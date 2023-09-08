<%@ page import="spring.data.Person" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: jiazh
  Date: 2022/9/12
  Time: 15:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<% Person person = (Person) session.getAttribute("person"); %>
<p>
  @SessionAttributes用于在请求之间的 HTTP 服务会话中存储模型属性。它是一个类型级批注，用于声明特定控制器使用的会话属性。<br />
  这通常会列出模型属性的名称或模型属性的类型，这些属性应透明地存储在会话中，以供后续请求访问。<br />
  注意：前提是必须提前在Model中添加一个属性(如: person)，对应的执行操作为：<br />
  model.addAttribute("person", new Person())或model.addAttribute(new Person())或使用全局注册Model级别的@ModelAttribute注解(在方法上使用)<br />
  person的值为：<%= person %>
</p>
<sf:form method="post" modelAttribute="person">
  <label for="name">name: </label>
  <input id="name" name="name" placeholder="${person.name}" /><br />

  <label for="age">age: </label>
  <input id="age" name="age" placeholder="${person.age}" /><br />

  <label for="email">email: </label>
  <input id="email" name="email" placeholder="${person.email}" /><br />

  <label for="date">Date: </label>
  <input id="date" name="date" type="date" /><br />

  <input type="submit" value="添加一个Person" />
</sf:form>
</body>
</html>
