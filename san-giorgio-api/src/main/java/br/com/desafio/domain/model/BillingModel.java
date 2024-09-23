package br.com.desafio.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "BILLING")
public class BillingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billingId;
    private BigDecimal valueOrigin;

    public Long getBillingId() {
        return billingId;
    }

    public void setBillingId(Long billingId) {
        this.billingId = billingId;
    }

    public BigDecimal getValueOrigin() {
        return valueOrigin;
    }

    public void setValueOrigin(BigDecimal valueOrigin) {
        this.valueOrigin = valueOrigin;
    }
}
