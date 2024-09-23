package br.com.desafio.service;

import br.com.desafio.domain.model.BillingModel;
import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.domain.model.SellerModel;
import br.com.desafio.repository.BillingRepository;
import br.com.desafio.repository.PaymentRepository;
import br.com.desafio.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private SqsClient sqsClient;  // Adicione este mock

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessPayment_BillingNotFound() {
        // Configurando o mock para retornar um vendedor válido e uma fatura vazia
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(new SellerModel()));
        when(billingRepository.findById(2L)).thenReturn(Optional.empty());

        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setBillingID(2L);
        paymentModel.setValuePayment(List.of(new BigDecimal("50.00")));

        // Verificando se a exceção é lançada quando a fatura não é encontrada
        assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(1L, paymentModel));
    }

    @Test
    public void testProcessPayment_SellerNotFound() {
        // Configurando o mock para retornar uma fatura válida e um vendedor vazio
        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());
        when(billingRepository.findById(2L)).thenReturn(Optional.of(new BillingModel()));

        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setBillingID(2L);
        paymentModel.setValuePayment(List.of(new BigDecimal("50.00")));

        // Verificando se a exceção é lançada quando o vendedor não é encontrado
        assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(1L, paymentModel));
    }

    @Test
    public void testProcessPayment_ValidPartialPayment() {
        BillingModel billing = new BillingModel();
        billing.setValueOrigin(new BigDecimal("100.00"));

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(new SellerModel()));
        when(billingRepository.findById(1L)).thenReturn(Optional.of(billing));

        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setBillingID(1L);
        paymentModel.setValuePayment(List.of(new BigDecimal("50.00")));

        paymentService.processPayment(1L, paymentModel);

        verify(sqsClient, times(1)).sendMessage((SendMessageRequest) any()); // Verifica se o método sendMessage foi chamado
        assertEquals("PARTIAL", paymentModel.getStatus());
    }

    @Test
    public void testProcessPayment_ValidTotalPayment() {
        BillingModel billing = new BillingModel();
        billing.setValueOrigin(new BigDecimal("100.00"));

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(new SellerModel()));
        when(billingRepository.findById(1L)).thenReturn(Optional.of(billing));

        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setBillingID(1L);
        paymentModel.setValuePayment(List.of(new BigDecimal("100.00")));

        paymentService.processPayment(1L, paymentModel);

        verify(sqsClient, times(1)).sendMessage((SendMessageRequest) any()); // Verifica se o método sendMessage foi chamado
        assertEquals("TOTAL", paymentModel.getStatus());
    }

    @Test
    public void testProcessPayment_ValidExcessPayment() {
        BillingModel billing = new BillingModel();
        billing.setValueOrigin(new BigDecimal("100.00"));

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(new SellerModel()));
        when(billingRepository.findById(1L)).thenReturn(Optional.of(billing));

        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setBillingID(1L);
        paymentModel.setValuePayment(List.of(new BigDecimal("150.00")));

        paymentService.processPayment(1L, paymentModel);

        verify(sqsClient, times(1)).sendMessage((SendMessageRequest) any()); // Verifica se o método sendMessage foi chamado
        assertEquals("EXCEDENT", paymentModel.getStatus());
    }
}
