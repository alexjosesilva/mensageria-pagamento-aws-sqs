package br.com.desafio.domain.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SellerModelTest {

    @Test
    public void testGetSetSellerId() {
        SellerModel seller = new SellerModel();
        Long expectedId = 1L;

        seller.setSellerId(expectedId);

        assertEquals(expectedId, seller.getSellerId());
    }

    @Test
    public void testGetSetNameSeller() {
        SellerModel seller = new SellerModel();
        String expectedName = "John Doe";

        seller.setNameSeller(expectedName);

        assertEquals(expectedName, seller.getNameSeller());
    }
}