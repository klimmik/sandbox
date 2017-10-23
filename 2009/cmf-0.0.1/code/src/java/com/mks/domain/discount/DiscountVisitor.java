package com.mks.domain.discount;

public interface DiscountVisitor {

    <T> T accept(DiscountVisitorAdapter<T> visitor);

}
