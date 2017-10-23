package com.mks.service.util.validation.rule;

import com.mks.domain.util.MonoWork;
import com.mks.domain.util.description.EntityInfo;
import com.mks.domain.util.description.Field;
import com.mks.domain.util.message.MsgInfo;
import com.mks.domain.util.message.ParamInfo;
import com.mks.domain.Identifiable;
import com.mks.service.util.validation.ValidationException;
import com.mks.service.util.validation.ValidationRule;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class UniqueFieldsRule extends ValidationRule {
    private static final String RESOURCE_VALIDATION_UNIQUE_FIELDS_VIOLATION = "mg.validation.uniqueFieldsViolation";

    public void validate(Object entity) throws ValidationException {

        UniquenessValidator validator = new UniquenessValidator();

        validator.doWork(entity.getClass().getName(), entity);

        if ( ! validator.getFieldsViolated().isEmpty() ) {
            MsgInfo.MsgResource msgResource = new MsgInfo.MsgResource(RESOURCE_VALIDATION_UNIQUE_FIELDS_VIOLATION);
            for (String field : validator.getFieldsViolated()) {
                msgResource.addParam(new ParamInfo.ParamFieldName(field));
            }
            String msg = "Unique constraint violation: " + validator.getFieldsViolated();
            throw new ValidationException(msg, msgResource);
        }
    }

    private class UniquenessValidator extends MonoWork {
        private List<String> fieldsViolated = new ArrayList<String>();

        public List<String> getFieldsViolated() {
            return fieldsViolated;
        }

        @Override
        protected boolean skipIdentifier() {
            return true;
        }

        @Override
        protected void handleField(EntityInfo info, Object object, Field field, Object value) {
            if (field.isUnique() && value != null) {
                Query query = getEntityManager()
                        .createQuery("from " + info.getClazz().getSimpleName() + " entity where entity." +
                                field.getName() + " = :value");

                query.setParameter("value", value);

                List list = query.getResultList();

                if ( ! list.isEmpty() ) {
                    Serializable id1 = ((Identifiable) object).getId();
                    Serializable id2 = ((Identifiable) list.iterator().next()).getId();
                    if ((id1 == null) || ( ! id1.equals(id2) )) {
                        //TODO consider full path to fileds in associations
                        fieldsViolated.add(info.getClazz().getSimpleName() + "." + field.getName());
                    }
                }
            }
        }
    }
}
