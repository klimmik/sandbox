package com.mks.service.util.validation.rule;

import com.mks.domain.offer.Offer;
import com.mks.domain.util.message.MsgInfo;
import com.mks.service.util.validation.ValidationException;
import com.mks.service.util.validation.ValidationRule;

public class OfferUpdateRule extends ValidationRule {
    private static final String RESOURCE_VALIDATION_OFFER_NOTES_REQUIRED = "mg.validation.update.offerNotesRequired";

    public void validate(Object entity) throws ValidationException {

        Offer offer = (Offer) entity;

        if ( offer.getOfferNotes().size() < 2 ) {
            MsgInfo.MsgResource msg = new MsgInfo.MsgResource(RESOURCE_VALIDATION_OFFER_NOTES_REQUIRED);
            throw new ValidationException("Offer must have at least two offer notes.", msg);
        }
    }
}