package com.mks.domain.discount;

import com.mks.domain.annotation.XEntity;
import com.mks.domain.annotation.XField;

import javax.persistence.Entity;
import javax.persistence.Column;

@Entity
@XEntity(topLevel = true, url = "amount-discount", displayProperty = "name")
public class AmountDiscount extends Discount {

    @Column(name = "AMOUNT")
    @XField(nullable = false)
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public <T> T accept(DiscountVisitorAdapter<T> visitor) {
        return visitor.visit(this);
    }
}
