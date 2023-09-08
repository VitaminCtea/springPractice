<%--
  Created by IntelliJ IDEA.
  User: jiazh
  Date: 2022/8/9
  Time: 21:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Spring Pizza</title>
</head>
<body>
<h2>Delivery Unavailable</h2>
<p>The address is outside of our delivery area. You may still place the order, but you will need to pick it up yourself.</p>
<a href="${flowExecutionUrl}&_eventId=accept">Accept</a> |
<a href="${flowExecutionUrl}&_eventId=cancel">Cancel</a>
</body>
</html>
