package com.mks.domain.discount;

public abstract class DiscountVisitorAdapter<T> {

    public T visit(Discount discount) {
        return null;
    }

    public T visit(AmountDiscount discount) {
        return null;
    }

    public T visit(PercentDiscount discount) {
        return null;
    }
}
