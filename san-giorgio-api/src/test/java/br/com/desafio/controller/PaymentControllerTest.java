package br.com.desafio.controller;

import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessPayment() {
        // Dados de entrada
        Long sellerId = 1L;
        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setBillingID(1L);
        paymentModel.setValuePayment(List.of(new BigDecimal("100.00")));

        // Mock do serviço
        when(paymentService.processPayment(sellerId, paymentModel)).thenReturn(paymentModel);

        // Chamada ao método do controlador
        ResponseEntity<PaymentModel> response = paymentController.processPayment(sellerId, paymentModel);

        // Verificações
        assertEquals(ResponseEntity.ok(paymentModel), response);
        assertEquals(200, response.getStatusCodeValue());
    }
}
