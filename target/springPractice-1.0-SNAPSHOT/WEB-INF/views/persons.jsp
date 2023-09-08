<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: jiazh
  Date: 2022/9/9
  Time: 16:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Persons</title>
</head>
<body>
    <sf:form method="post" modelAttribute="person">
        <sf:label path="name">name: </sf:label>
        <sf:input path="name" /><br />

        <sf:label path="age">age: </sf:label>
        <sf:input path="age" /><br />

        <sf:label path="email">email: </sf:label>
        <sf:input path="email" /><br />

        <input type="submit" value="添加一个Person" />
    </sf:form>
</body>
</html>
