<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Пользователи</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid black; padding: 8px; text-align: left; }
    </style>
</head>
<body>
    <h1>Управление пользователями</h1>

    <h3>Добавить пользователя</h3>
    <form method="post">
        <input type="hidden" name="action" value="create">
        Username: <input type="text" name="username" required><br><br>
        Password: <input type="password" name="password" required><br><br>
        Role:
        <select name="role">
            <option value="CLIENT">CLIENT</option>
            <option value="ADMIN">ADMIN</option>
        </select><br><br>
        <button type="submit">Добавить пользователя</button>

        <input type="hidden"  name="action" value="update">
            <input type="number" name="id" placeholder="ID">
            <input type="text" name="username" placeholder="Username">
            <input type="text" name="password" placeholder="Password">
            <input type="text" name="role" placeholder="Role">
            <button type="submit">Update</button>
    </form>

    <hr>

    <h3>Список пользователей</h3>
    <table>
        <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Role</th>
            <th>Действие</th>
        </tr>
        <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.role}</td>
                <td>
                    <form method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${user.id}">
                        <button type="submit" onclick="return confirm('Удалить пользователя?')">Удалить</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>

    <br>
    <a href="${pageContext.request.contextPath}/products">→ Товары</a>
    <a href="${pageContext.request.contextPath}/orders">Заказы</a>
</body>
</html>