<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://mks.org/jsp/jstl/mks" prefix="mks" %>

<bean:define id="so" name="SESSION_OBJECT" scope="session" type="com.mks.app.action.SessionObject" />
<bean:define id="info" name="so" property="info" type="com.mks.domain.util.description.EntityInfo" />
<bean:define id="entity" name="so" property="entityForm" type="com.mks.app.form.manager.EntityForm" />
<bean:define id="msgKey"><mks:uncapitalizedSimpleName clazz="${info.clazz}" /></bean:define>

<h1><fmt:message key="mg.${msgKey}.h1" /></h1>

<script type="text/javascript">
    var validate = true;
    function validateForm() {
        if (validate) {
            return validateDetails();
        }
        return true;
    }
    function setValidate(value) {
        validate = value;
    }
</script>

<div id="messages" class="messages" <logic:messagesNotPresent>style="display: none"</logic:messagesNotPresent>>
    <logic:messagesPresent>
        <html:messages id="errMsg">
        <span><bean:write filter="false" name="errMsg"/></span><br />
        </html:messages>
    </logic:messagesPresent>
</div>

<html:form action="/update/" styleId="entityForm" onsubmit="return validateForm(this)">
    <table class="edit-table">
        <thead>
            <tr>
                <th width="30%"><fmt:message key="mg.list.col.property" /></th>
                <th width="70%"><fmt:message key="mg.list.col.value" /></th>
            </tr>
        </thead>
        <tbody>
            <logic:iterate id="field" name="info" property="fields" type="com.mks.domain.util.description.Field">
            <tr>
                <bean:define id="fieldMsgKey"><mks:uncapitalizedSimpleName clazz="${field.parentInfo.clazz}" /></bean:define>
                <td><fmt:message key="mg.names.${fieldMsgKey}.${field.name}" />: <c:if test="${not field.nullable}"><span style="color: red;">*</span></c:if></td> <%-- use css instead --%>
                <bean:define id="fieldValue" value="" />
                <logic:notEmpty name="entity" property="properties(${field.name})">
                <bean:define id="fieldValue"><bean:write name="entity" property="properties(${field.name})" /></bean:define>
                </logic:notEmpty>
                <td><mks:choose>
                    <mks:whenBoolean clazz="${field.clazz}">
                        <html:hidden property="properties(${field.name})" value="${fieldValue}" styleId="${field.name}" />
                        <input type="checkbox" <c:if test="${fieldValue}">checked</c:if>
                                onclick="updateFormBooleanField(this, '${field.name}')"
                                onchange="updateFormBooleanField(this, '${field.name}')" />
                    </mks:whenBoolean>
                    <mks:whenEnum clazz="${field.clazz}">
                        <html:select property="properties(${field.name})" value="${fieldValue}" styleId="${field.name}">
                            <html:option value="" key="mg.select.notSelected" />
                            <html:options name="field" property="clazz.enumConstants" />
                        </html:select>
                    </mks:whenEnum>
                    <c:otherwise>
                        <html:text property="properties(${field.name})" value="${fieldValue}" maxlength="${field.length}" styleId="${field.name}" disabled="${field.name eq 'id'}"/>
                    </c:otherwise>
                </mks:choose></td>
            </tr>
            </logic:iterate>

            <logic:iterate id="association" name="info" property="associations" type="com.mks.domain.util.description.Association">
            <bean:define id="associationMsgKey"><mks:uncapitalizedSimpleName clazz="${association.parentInfo.clazz}" /></bean:define>
            <c:if test="${not association.type.collection and association.composition}">
            <tr>
                <td><fmt:message key="mg.names.${associationMsgKey}.${association.name}" />:</td>
                <td>
                    <logic:notEmpty name="entity" property="properties(${association.name})">
                    <bean:define id="ass" name="entity" property="properties(${association.name})" type="com.mks.app.form.manager.EntityForm" />
                    <table class="edit-table-associations">
                        <tr>
                            <td width="60%"><bean:write name="ass" property="properties(${association.displayProperty})" /></td>
                            <td width="20%" align="center"><input type="button" value="<fmt:message key="mg.butt.edit" />" onclick="editItem('${association.name}')" /></td>
                            <td width="20%" align="center"><input type="button" value="<fmt:message key="mg.butt.remove" />" onclick="removeItem('${association.name}')" /></td>
                        </tr>
                    </table>
                    </logic:notEmpty>
                    <logic:empty name="entity" property="properties(${association.name})">
                    <input type="button" value="<fmt:message key="mg.butt.add" />" onclick="addItem('${association.name}')" />
                    </logic:empty>
                </td>
            </tr>
            </c:if>
            </logic:iterate>

            <logic:iterate id="association" name="info" property="associations" type="com.mks.domain.util.description.Association">
            <bean:define id="associationMsgKey"><mks:uncapitalizedSimpleName clazz="${association.parentInfo.clazz}" /></bean:define>
            <c:if test="${association.type.collection and association.composition}">
            <tr>
                <td><fmt:message key="mg.names.${associationMsgKey}.${association.name}" />:</td>
                <td>
                    <logic:notEmpty name="entity" property="properties(${association.name})">
                    <logic:iterate id="ass" name="entity" property="properties(${association.name})" type="com.mks.app.form.manager.EntityForm">
                    <table class="edit-table-associations">
                        <tr>
                            <td width="60%"><bean:write name="ass" property="properties(${association.displayProperty})" /></td>
                            <td width="20%" align="center"><input type="button" value="<fmt:message key="mg.butt.edit" />" onclick="editItem('${association.name}', '<mks:hashCode object="${ass}" />')" /></td>
                            <td width="20%" align="center"><input type="button" value="<fmt:message key="mg.butt.remove" />" onclick="removeItem('${association.name}', '<mks:hashCode object="${ass}" />')" /></td>
                        </tr>
                    </logic:iterate>
                    </table>
                    </logic:notEmpty>
                    <input type="button" value="<fmt:message key="mg.butt.add" />" onclick="addItem('${association.name}')" />
                </td>
            </tr>
            </c:if>
            </logic:iterate>

            <logic:iterate id="association" name="info" property="associations" type="com.mks.domain.util.description.Association">
            <bean:define id="associationMsgKey"><mks:uncapitalizedSimpleName clazz="${association.parentInfo.clazz}" /></bean:define>
            <c:if test="${not association.type.collection and not association.composition}">
            <tr>
                <td><fmt:message key="mg.names.${associationMsgKey}.${association.name}" />:</td>
                <td>
                    <logic:notEmpty name="entity" property="properties(${association.name})">
                    <bean:define id="ass" name="entity" property="properties(${association.name})" type="com.mks.app.form.manager.EntityForm" />
                    <table class="edit-table-associations">
                        <tr>
                            <td width="70%"><bean:write name="ass" property="properties(${association.displayProperty})" /></td>
                            <td width="30%" align="center"><input type="button" value="<fmt:message key="mg.butt.remove" />" onclick="removeItem('${association.name}')" /></td>
                        </tr>
                    </table>
                    </logic:notEmpty>
                    <input type="button" value="<fmt:message key="mg.butt.select" />" onclick="addItem('${association.name}')" />
                </td>
            </tr>
            </c:if>
            </logic:iterate>

            <logic:iterate id="association" name="info" property="associations" type="com.mks.domain.util.description.Association">
            <bean:define id="associationMsgKey"><mks:uncapitalizedSimpleName clazz="${association.parentInfo.clazz}" /></bean:define>
            <c:if test="${association.type.collection and not association.composition}">
            <tr>
                <td><fmt:message key="mg.names.${associationMsgKey}.${association.name}" />:</td>
                <td>
                    <logic:notEmpty name="entity" property="properties(${association.name})">
                    <logic:iterate id="ass" name="entity" property="properties(${association.name})" type="com.mks.app.form.manager.EntityForm">
                    <table class="edit-table-associations">
                        <tr>
                            <td width="70%"><bean:write name="ass" property="properties(${association.displayProperty})" /></td>
                            <td width="30%" align="center"><input type="button" value="<fmt:message key="mg.butt.remove" />" onclick="removeItem('${association.name}', '<mks:hashCode object="${ass}" />')" /></td>
                        </tr>
                    </logic:iterate>
                    </table>
                    </logic:notEmpty>
                    <input type="button" value="<fmt:message key="mg.butt.add" />" onclick="addItem('${association.name}')" />
                </td>
            </tr>
            </c:if>
            </logic:iterate>
        </tbody>
        <tfoot>
            <tr><th colspan="2"><html:submit style="float:right"><fmt:message key="mg.butt.submit" /></html:submit><html:cancel onclick="setValidate(false)"><fmt:message key="mg.butt.cancel" /></html:cancel></th></tr>
        </tfoot>
    </table>
</html:form>

<script type="text/javascript">
    function updateFormBooleanField(control, formFieldStyleId) {
        document.getElementById(formFieldStyleId).value = (control.checked) ? 'true' : 'false';
    }

    function addItem(associationName) {
        submitItemAction('<html:rewrite action="/add-item/" />', associationName);
    }
    /**
     * @param itemIndex required for one-to-many and many-to-many associations.
     */
    function removeItem(associationName, itemIndex) {
        submitItemAction('<html:rewrite action="/remove-item/" />', associationName, itemIndex);
    }
    /**
     * @param itemIndex required for one-to-many associations.
     */
    function editItem(associationName, itemIndex) {
        submitItemAction('<html:rewrite action="/edit-item/" />', associationName, itemIndex);
    }

    function submitItemAction(actionName, associationName, itemIndex) {
        var entityForm = document.getElementById('entityForm');
        var params = itemIndex != undefined ? '&itemIndex=' + itemIndex : '';
        entityForm.action = actionName + '?association=' + associationName + params;
        entityForm.submit();
    }
</script>

<script type="text/javascript">
    function validateDetails() {
        var count = 0;
        var existenceMessage = '';
        var maskMessage = '';
        var focusField = null;
        var fieldValue;

        <logic:iterate id="field" name="info" property="fields" type="com.mks.domain.util.description.Field">
        <bean:define id="fieldMsgKey"><mks:uncapitalizedSimpleName clazz="${field.parentInfo.clazz}" /></bean:define>
        fieldValue = trim(document.getElementById('${field.name}').value);
        <%-- BEGIN existence validation --%>
        <c:if test="${not field.nullable}">
        if (fieldValue.length == 0) {
            count++;
            if (count == 1) {
                focusField = document.getElementById('${field.name}');
            }
            existenceMessage += (existenceMessage.length > 0 ? ', ' : '<fmt:message key="mg.commonValidation.required" /> ') + '<strong><fmt:message key="mg.names.${fieldMsgKey}.${field.name}" /></strong>';
        }
        </c:if>
        <%-- END existence validation --%>
        <%-- BEGIN mask validation --%>
        if (fieldValue.length != 0 && fieldValue.search( new RegExp('${field.validationMask.valueJS}') ) == -1) {
            count++;
            if (count == 1) {
                focusField = document.getElementById('${field.name}');
            }
            maskMessage += (maskMessage.length > 0 ? '<br />' : '<fmt:message key="mg.commonValidation.mask" /> ') + '<strong><fmt:message key="mg.names.${fieldMsgKey}.${field.name}" /></strong>' + '. ' + '<fmt:message key="mg.commonValidation.mask.template.${field.validationMask.templateMsgKey}" />';
        }
        <%-- END mask validation --%>
        </logic:iterate>

        if (existenceMessage.length > 0) {
            existenceMessage += ".";
        }

        var message = existenceMessage;
        if (existenceMessage.length > 0 && maskMessage.length > 0) {
            message += '<br />';
        }
        message += maskMessage;

        if (count > 0) {
            document.getElementById('messages').innerHTML = message;
            document.getElementById('messages').style.display = 'block';
            focusField.focus();
            return false;
        }

        return true;
    }

    function trim(s) {
        return s.replace( /^\s*/, '' ).replace( /\s*$/, '' );
    }
</script>