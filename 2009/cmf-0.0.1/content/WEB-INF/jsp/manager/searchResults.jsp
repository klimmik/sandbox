<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://mks.org/jsp/jstl/mks" prefix="mks" %>
<bean:define id="so" name="SESSION_OBJECT" scope="session" type="com.mks.app.action.SessionObject" />
<bean:define id="self" name="backingBean" scope="request" type="com.mks.app.bean.manager.SearchResultsBean" />
<bean:define id="msgKey"><mks:uncapitalizedSimpleName clazz="${self.info.clazz}" /></bean:define>
<h1><fmt:message key="mg.${msgKey}.h1" /></h1>

<div id="messages" class="messages" <logic:messagesNotPresent>style="display: none"</logic:messagesNotPresent>>
    <logic:messagesPresent>
        <html:messages id="errMsg">
        <span><bean:write filter="false" name="errMsg"/></span><br />
        </html:messages>
    </logic:messagesPresent>
</div>

<c:if test="${not so.state.selectState and self.info.topLevel}"><%--TODO: replace second condition - in case of false let user to choose which top-level to create --%><html:link action="/add/${self.info.url}/"><fmt:message key="mg.butt.add" /></html:link><br /><br /></c:if>
<c:if test="${so.state.selectState}">
<form action="<html:rewrite action="/select/" />" method="post" id="selectForm">
    <input type="hidden" name="identifiers" id="identifiers" />
</c:if>
<logic:notEmpty name="self" property="entities">
<table class="search-results">
    <thead>
        <tr>
            <c:if test="${not so.state.selectState}">
            <th width="70%"><fmt:message key="mg.list.col.name" /></th>
            <th width="15%">&nbsp;</th>
            <th width="15%">&nbsp;</th>
            </c:if>
            <c:if test="${so.state.selectState && so.association.type.collection}">
            <th width="15%">&nbsp;</th>
            <th width="85%"><fmt:message key="mg.list.col.name" /></th>
            </c:if>
            <c:if test="${so.state.selectState && not so.association.type.collection}">
            <th width="90%"><fmt:message key="mg.list.col.name" /></th>
            <th width="10%">&nbsp;</th>
            </c:if>
        </tr>
    </thead>
    <tbody>
        <c:set var="loopIndex" />
        <logic:iterate id="entity" name="self" property="entities" type="com.mks.domain.Identifiable" indexId="loopIndex">
        <tr>
            <c:if test="${so.state.selectState && so.association.type.collection}">
            <td align="center"><input type="checkbox" id="choose${loopIndex}" value="${entity.id}" /></td>
            </c:if>
            <td><bean:write name="entity" property="${self.info.displayProperty}" /></td>
            <c:if test="${not so.state.selectState}">
            <td align="center"><html:link action="/edit/${self.info.url}/${entity.id}/"><fmt:message key="mg.butt.edit" /></html:link></td>
            <td align="center"><html:link action="/remove/${self.info.url}/${entity.id}/"><fmt:message key="mg.butt.remove" /></html:link></td>
            </c:if>
            <c:if test="${so.state.selectState && not so.association.type.collection}">
            <td align="center"><input type="button" value="<fmt:message key="mg.butt.select" />" onclick="selectItem(${entity.id})" /></td>
            </c:if>
        </tr>
    </logic:iterate>
    </tbody>
    <c:if test="${so.state.selectState}">
    <tfoot>
        <tr><th colspan="2"><html:cancel><fmt:message key="mg.butt.cancel" /></html:cancel>&nbsp;&nbsp;&nbsp;<c:if test="${so.association.type.collection}"><input type="button" value="<fmt:message key="mg.butt.select" />" onclick="selectItems()" /></c:if></th></tr>
    </tfoot>
    </c:if>
</table>
<c:if test="${so.state.selectState}">
    <c:if test="${so.association.type.collection}">
    <script type="text/javascript">
        function selectItems() {
            var index = 0;
            var element = document.getElementById('choose' + index);
            var result = '';
            while (element != null) {
                if (element.checked) {
                    result = result + ((result.length > 0) ? ',' : '') + element.value;
                }
                index++;
                element = document.getElementById('choose' + index); <%-- not elegant coz it's a copy-paste of the string above --%>
            }
            if (result.length == 0) {
                window.alert('nothing selected (todo: get this message from properties)');
                return;
            }
            document.getElementById('identifiers').value = result;
            document.getElementById('selectForm').submit();
        }
    </script>
    </c:if>
    <c:if test="${not so.association.type.collection}">
    <script type="text/javascript">
        function selectItem(id) {
            document.getElementById('identifiers').value = id;
            document.getElementById('selectForm').submit();
        }
    </script>
    </c:if>
</c:if>
</logic:notEmpty>
<logic:empty name="self" property="entities">
<fmt:message key="mg.${msgKey}.notfound" />&nbsp;&nbsp;&nbsp;<%-- TODO: style to css --%><c:if test="${so.state.selectState}"><html:cancel><fmt:message key="mg.butt.back" /></html:cancel></c:if>
<c:if test="${not so.state.selectState and self.info.topLevel}"><%--TODO: actually this is some kind of copy-paste condition..... anyway, replace second condition - in case of false let user to choose which top-level to create --%>
<fmt:message key="mg.list.quest.add.pre" />
<html:link action="/add/${self.info.url}/"><fmt:message key="mg.list.quest.add.act" /></html:link>
<fmt:message key="mg.list.quest.add.post" />
</c:if>
</logic:empty>
<c:if test="${so.state.selectState}">
</form>
</c:if>
