<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: jiazh
  Date: 2022/7/27
  Time: 21:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Spittr</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css" />">
</head>
<body>
    <h1>Register</h1>
    <sf:form method="post" modelAttribute="spitter">
        <sf:label path="firstName">First Name: </sf:label>
        <sf:input path="firstName" />
        <sf:errors path="firstName" /><br/>

        <sf:label path="lastName">Last Name: </sf:label>
        <sf:input path="lastName" />
        <sf:errors path="lastName" /><br/>

        <sf:label path="username">Username: </sf:label>
        <sf:input path="username" />
        <sf:errors path="username" /><br/>

        <sf:label path="password">Password: </sf:label>
        <sf:password path="password" />
        <sf:errors path="password" /><br/>

        <input type="submit" value="Register" />
    </sf:form>
<%--    <form method="post">--%>
<%--        <label for="firstName">First Name:</label>--%>
<%--        <input id="firstName" type="text" name="firstName"><br />--%>

<%--        <label for="lastName">Last Name:</label>--%>
<%--        <input id="lastName" type="text" name="lastName"><br />--%>

<%--        <label for="username">Username:</label>--%>
<%--        <input id="username" type="text" name="username"><br />--%>

<%--        <label for="password">Password:</label>--%>
<%--        <input id="password" type="text" name="password"><br />--%>

<%--        <input type="submit" value="Register" />--%>
<%--    </form>--%>
</body>
</html>
