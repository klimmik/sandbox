package com.mks.service.util.validation;

import com.mks.domain.offer.Offer;
import com.mks.domain.offer.OfferItem;
import com.mks.domain.util.message.MsgInfo;
import com.mks.service.util.validation.rule.OfferItemRemoveRule;
import com.mks.service.util.validation.rule.OfferUpdateRule;
import com.mks.service.util.validation.rule.UniqueFieldsRule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import java.util.*;

public class Validator {
    private static final Log log = LogFactory.getLog(Validator.class);

    public static void validateUpdates(EntityManager em, Object entity) throws ValidationException {
        Set<String> rules = getRulesForEntityUpdates(entity.getClass().getName());

        validate(em, entity, rules);
    }

    public static void validateRemoval(EntityManager em, Object entity) throws ValidationException {
        Set<String> rules = getRulesForEntityRemoval(entity.getClass().getName());

        validate(em, entity, rules);
    }

    private static void validate(EntityManager em, Object entity, Set<String> rules) throws ValidationException {
        List<MsgInfo> errors = new ArrayList<MsgInfo>();

        for (String rule : rules) {
            try {
                ValidationRule vr = (ValidationRule) Class.forName(rule).newInstance();
                vr.setEntityManager(em);
                vr.validate(entity);
            } catch (ValidationException e) {
                log.debug(e.getMessage());
                errors.addAll(e.getMsgInfoList());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        if ( ! errors.isEmpty() ) {
            throw new ValidationException(errors);
        }
    }

    private static Set<String> getRulesForEntityUpdates(String entityType) {
        Set<String> res = new HashSet<String>();

        res.add(UniqueFieldsRule.class.getName());

        Set<String> rules = UPDATE_RULES.get(entityType);
        if (rules != null) {
            res.addAll(rules);
        }

        return res;
    }

    private static Set<String> getRulesForEntityRemoval(String entityType) {
        Set<String> res = new HashSet<String>();

        Set<String> rules = REMOVE_RULES.get(entityType);
        if (rules != null) {
            res.addAll(rules);
        }

        return res;
    }

    //TODO retrieve this info from XML
    private static final Map<String, Set<String>> UPDATE_RULES = new LinkedHashMap<String, Set<String>>();
    private static final Map<String, Set<String>> REMOVE_RULES = new LinkedHashMap<String, Set<String>>();

    static {
        UPDATE_RULES.put(Offer.class.getName(), Collections.singleton(OfferUpdateRule.class.getName()));

        //TODO create common removal rules, if all rules are fulfilled then dereference an entity and remove
        REMOVE_RULES.put(OfferItem.class.getName(), Collections.singleton(OfferItemRemoveRule.class.getName()));
    }
}
