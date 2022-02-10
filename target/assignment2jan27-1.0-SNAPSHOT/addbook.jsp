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
    <title>AddBook</title>
</head>
<body>
<h1>Add A Book</h1>
<form method="post" action="LibraryData">
    <input type="hidden" name="type" value="book">
    <label for="title">Title</label><input type="text" id="title" name="title">
    <br />
    <label>Author</label>
    <br />
    <label>First Name</label><input type="text" id="firstName" name="firstName">
    <label>Last Name</label><input type="text" id="lastName" name="lastName">
    <br />
    <label for="edition">Edition</label><input type="text" id="edition" name="edition">
    <br />
    <label for="copyright">Copyright</label><input type="text" id="copyright" name="copyright">
    <br />
    <label for="isbn">ISBN</label><input type="text" id="isbn" name="isbn">
    <br />
    <input type="submit">
</form>
<a href="index.jsp">Home</a>
</body>
</html>
