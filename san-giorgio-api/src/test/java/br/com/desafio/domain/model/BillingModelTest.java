package br.com.desafio.domain.model;

import br.com.desafio.domain.model.BillingModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BillingModelTest {

    @Test
    public void testGetSetBillingId() {
        BillingModel billing = new BillingModel();
        Long expectedId = 1L;

        billing.setBillingId(expectedId);

        assertEquals(expectedId, billing.getBillingId());
    }

    @Test
    public void testGetSetValueOrigin() {
        BillingModel billing = new BillingModel();
        BigDecimal expectedValue = new BigDecimal("100.00");

        billing.setValueOrigin(expectedValue);

        assertEquals(expectedValue, billing.getValueOrigin());
    }
}