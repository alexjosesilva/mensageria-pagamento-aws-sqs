package br.com.desafio.domain.model;

import java.math.BigDecimal;

public class PaymentModel {
    private Long billingID;
    private BigDecimal valuePayment;
    private String status;

    public Long getBillingID() {
        return billingID;
    }

    public void setBillingID(Long billingID) {
        this.billingID = billingID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValuePayment() {
        return valuePayment;
    }

    public void setValuePayment(BigDecimal valuePayment) {
        this.valuePayment = valuePayment;
    }

    public String getStatus() {
        return status;
    }
}
