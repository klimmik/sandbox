package com.mks.domain.discount;

import com.mks.domain.Identifiable;
import com.mks.domain.annotation.XEntity;
import com.mks.domain.annotation.XField;

import javax.persistence.*;

@Entity @Table(name = "DISCOUNT")
@XEntity(url = "discount", displayProperty = "name")
public abstract class Discount implements DiscountVisitor, Identifiable<Long> {

    @Id @GeneratedValue
    @Column(name = "DISCOUNT_ID") 
    @XField
    private Long id;
    @Column(name = "NAME", nullable = false)
    @XField
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public <T> T accept(DiscountVisitorAdapter<T> visitor) {
        return visitor.visit(this);
    }
}
