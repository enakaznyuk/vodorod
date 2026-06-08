<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Заказы</title>
</head>
<body>

<h1>Заказы</h1>

<h3>Создать заказ</h3>

<form method="post">
    ID пользователя:
    <input type="number" name="userId" required>
    <button type="submit">Создать</button>
</form>

<br>

<table border="1">
<tr>
    <th>ID</th>
    <th>User ID</th>
    <th>Username</th>
    <th>Дата</th>
</tr>

<c:forEach var="o" items="${orders}">
<tr>
    <td>${o.id}</td>
    <td>${o.userId}</td>
    <td>${o.username}</td>
    <td>${o.orderDate}</td>
</tr>
</c:forEach>

</table>

<br>
<a href="${pageContext.request.contextPath}/users">Пользователи</a>
<br>
<a href="${pageContext.request.contextPath}/products">Товары</a>

</body>
</html>