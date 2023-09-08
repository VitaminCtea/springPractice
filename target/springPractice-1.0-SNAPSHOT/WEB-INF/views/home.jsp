<%-- JSP标准标签库 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- spring JSP标准库 --%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="false" %>
<html>
<head>
    <title>Spittr</title>
    <link rel="stylesheet" type="text/css" href="<s:theme code="styleSheet"/>" />
</head>
<body>
    <div>
        <a href="?theme=dark">Dark Theme</a><br />
        <a href="?theme=light">Light Theme</a>
    </div>
    <h1 class="h1"><s:message code="spittr.welcome" /></h1>
    <a href="<c:url value="/spittles" />">Spittles</a>
    <a href="<c:url value="/spitter/register" />">Register</a>
</body>
</html>
