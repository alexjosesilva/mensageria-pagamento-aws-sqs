package br.com.desafio.service;

import br.com.desafio.domain.model.BillingModel;
import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.domain.model.SellerModel;
import br.com.desafio.repository.BillingRepository;
import br.com.desafio.repository.PaymentRepository;
import br.com.desafio.repository.SellerRepository;
import br.com.desafio.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private PaymentRepository paymentRepository;

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
        // Configurando mocks para retornar um vendedor e fatura válidos
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(new SellerModel()));
        BillingModel billing = new BillingModel();
        billing.setValueOrigin(new BigDecimal("100.00"));
        when(billingRepository.findById(2L)).thenReturn(Optional.of(billing));

        // Criando um modelo de pagamento válido com pagamento parcial
        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setBillingID(2L);
        paymentModel.setValuePayment(List.of(new BigDecimal("50.00")));

        // Executando o método de pagamento
        paymentService.processPayment(1L, paymentModel);

        // Verificando se o pagamento foi salvo no repositório
        verify(paymentRepository, times(1)).save(any(PaymentModel.class));
    }

    @Test
    public void testProcessPayment_ValidTotalPayment() {
        // Configurando mocks para retornar um vendedor e fatura válidos
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(new SellerModel()));
        BillingModel billing = new BillingModel();
        billing.setValueOrigin(new BigDecimal("100.00"));
        when(billingRepository.findById(2L)).thenReturn(Optional.of(billing));

        // Criando um modelo de pagamento válido com pagamento total
        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setBillingID(2L);
        paymentModel.setValuePayment(List.of(new BigDecimal("100.00")));

        // Executando o método de pagamento
        paymentService.processPayment(1L, paymentModel);

        // Verificando se o pagamento foi salvo no repositório
        verify(paymentRepository, times(1)).save(any(PaymentModel.class));
    }

    @Test
    public void testProcessPayment_ValidExcessPayment() {
        // Configurando mocks para retornar um vendedor e fatura válidos
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(new SellerModel()));
        BillingModel billing = new BillingModel();
        billing.setValueOrigin(new BigDecimal("100.00"));
        when(billingRepository.findById(2L)).thenReturn(Optional.of(billing));

        // Criando um modelo de pagamento válido com pagamento excedente
        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setBillingID(2L);
        paymentModel.setValuePayment(List.of(new BigDecimal("150.00")));

        // Executando o método de pagamento
        paymentService.processPayment(1L, paymentModel);

        // Verificando se o pagamento foi salvo no repositório
        verify(paymentRepository, times(1)).save(any(PaymentModel.class));
    }
}
