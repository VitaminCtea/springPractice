<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%--
  Created by IntelliJ IDEA.
  User: jiazh
  Date: 2022/8/1
  Time: 16:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Spittr</title>
    <link rel="stylesheet" type="text/css" href="<s:url value="/css/style.css" />">
</head>
<body>
    <div id="header">
        <t:insertAttribute name="header" />
    </div>

    <div id="content">
        <t:insertAttribute name="body" />
    </div>

    <div id="footer">
        <t:insertAttribute name="footer" />
    </div>
</body>
</html>
