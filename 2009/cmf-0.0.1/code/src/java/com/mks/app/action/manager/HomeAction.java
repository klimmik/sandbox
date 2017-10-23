package com.mks.app.action.manager;

import com.mks.app.action.manager.common.BaseManagerAction;
import com.mks.domain.offer.TermsAndConditions;
import com.mks.domain.offer.OfferItem;
import com.mks.domain.offer.Offer;
import com.mks.domain.offer.OfferNote;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;

public class HomeAction extends BaseManagerAction {

    protected ActionForward executeAction(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {

        if (request.getParameter("test") != null) {
            TermsAndConditions terms1 = new TermsAndConditions();
            terms1.setName("terms1");
            terms1.setDescription("desc1");
            getEntityService().saveOrUpdate(TermsAndConditions.class, terms1);
            TermsAndConditions terms2 = new TermsAndConditions();
            terms2.setName("terms2");
            terms2.setDescription("desc2");
            getEntityService().saveOrUpdate(TermsAndConditions.class, terms2);

            OfferItem item1 = new OfferItem();
            item1.setName("item1");
            item1.setPrice(123d);
            getEntityService().saveOrUpdate(OfferItem.class, item1);
            OfferItem item2 = new OfferItem();
            item2.setName("item2");
            item2.setPrice(432d);
            getEntityService().saveOrUpdate(OfferItem.class, item2);

            Offer offer = new Offer();
            offer.setName("offer");
            offer.setStartDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2007"));
            offer.setEndDate(new SimpleDateFormat("dd/MM/yyyy").parse("03/03/2009"));

            OfferNote offerNote1 = new OfferNote();
            offerNote1.setDescription("1");
            offer.getOfferNotes().add(offerNote1);
            OfferNote offerNote2 = new OfferNote();
            offerNote2.setDescription("2");
            offer.getOfferNotes().add(offerNote2);

            offer.setTermsAndConditions(getEntityService().getAll(TermsAndConditions.class).iterator().next());
            offer.getOfferItems().add(getEntityService().getAll(OfferItem.class).get(0));
            offer.getOfferItems().add(getEntityService().getAll(OfferItem.class).get(1));

            getEntityService().saveOrUpdate(Offer.class, offer);
        }

        clearSessionObject(request);

        return mapping.findForward(NEXT);
    }
}
