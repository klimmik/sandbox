package com.mks.domain.discount;

import com.mks.domain.annotation.XEntity;
import com.mks.domain.annotation.XField;

import javax.persistence.Entity;
import javax.persistence.Column;

@Entity
@XEntity(topLevel = true, url = "percent-discount", displayProperty = "name")
public class PercentDiscount extends Discount {

    @Column(name = "PERCENT"/*TODO, nullable = false*/)
    @XField
    private Float percent;

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    @Override
    public <T> T accept(DiscountVisitorAdapter<T> visitor) {
        return visitor.visit(this);
    }
}
