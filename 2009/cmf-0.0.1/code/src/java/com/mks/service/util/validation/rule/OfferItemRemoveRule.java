package com.mks.service.util.validation.rule;

import com.mks.domain.offer.OfferItem;
import com.mks.domain.util.message.MsgInfo;
import com.mks.service.util.validation.ValidationException;
import com.mks.service.util.validation.ValidationRule;

public class OfferItemRemoveRule extends ValidationRule {
    private static final String RESOURCE_VALIDATION_OFFER_ITEM_REF_BY_OFFER = "mg.validation.removal.offerItemRefByOffer";

    public void validate(Object entity) throws ValidationException {

        OfferItem item = (OfferItem) entity;

        if ( ! item.getOffers().isEmpty() ) {
            MsgInfo.MsgResource msg = new MsgInfo.MsgResource(RESOURCE_VALIDATION_OFFER_ITEM_REF_BY_OFFER);
            throw new ValidationException("Can't remove offer item, it's referenced by offer.", msg);
        }
    }
}