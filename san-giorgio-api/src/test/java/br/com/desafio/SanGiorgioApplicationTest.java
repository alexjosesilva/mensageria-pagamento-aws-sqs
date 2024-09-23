package br.com.desafio;

import br.com.desafio.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class SanGiorgioApplicationTest {

    @MockBean
    private PaymentService paymentService; // Substitui o PaymentService real por um mock

    @Test
    void contextLoads() {
        // Verifica se o contexto da aplicação carrega corretamente
    }
}
