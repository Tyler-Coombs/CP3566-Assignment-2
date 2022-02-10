<%--
  Created by IntelliJ IDEA.
  User: tylercoombs
  Date: 2022-01-27
  Time: 9:48 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>AddAuthor</title>
</head>
<body>
<h1>Add An Author</h1>
<form method="post" action="LibraryData">
    <input type="hidden" name="type" value="author">
    <label for="firstName">First Name</label><input type="text" id="firstName" name="firstName">
    <br />
    <label for="lastName">Last Name</label><input type="text" id="lastName" name="lastName">
    <br />
    <input type="submit">
</form>
<a href="index.jsp">Home</a>
</body>
</html>
