<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<tiles:importAttribute />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<title><fmt:message key="${title}" /></title>
<tiles:insert attribute="html-head" />
</head>
<body>
<div id="wrapper">
<logic:notEmpty name="header"><div id="header">
<tiles:insert attribute="header" />
</div></logic:notEmpty>
<div id="content">
<tiles:insert attribute="content" />
</div>
<logic:notEmpty name="footer"><div id="footer">
<tiles:insert attribute="footer" />
</div></logic:notEmpty>
</div>
</body>
</html>