<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: jiazh
  Date: 2022/8/9
  Time: 21:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Spring Pizza</title>
</head>
<body>
<h2>Thank you for your order!</h2>

<form:form>
  <input type="hidden" name="_flowExecutionKey"
         value="${flowExecutionKey}"/>
  <input type="submit" name="_eventId_finished" value="Finished" />
</form:form>

<form:form>
  <input type="hidden" name="_flowExecutionKey"
         value="${flowExecutionKey}"/>
  <input type="hidden" name="_eventId"
         value="finished" />
  <input type="submit" value="Finished" />
</form:form>


<a href='${flowExecutionUrl}&_eventId=finished'>Finish</a>
</body>
</html>
