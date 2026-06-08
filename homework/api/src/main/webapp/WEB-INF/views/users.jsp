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
    <jsp:include page="/WEB-INF/views/includes/navbar.jsp"/>
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
    </form>

    <c:if test="${not empty user}">
        <h3>Изменить пользователя #${user.id}</h3>
        <form method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="${user.id}">
            Username: <input type="text" name="username" value="${user.username}" required><br><br>
            Password: <input type="password" name="password" value="${user.password}" required><br><br>
            Role:
            <select name="role">
                <option value="CLIENT" <c:if test="${user.role == 'CLIENT'}">selected</c:if>>CLIENT</option>
                <option value="ADMIN" <c:if test="${user.role == 'ADMIN'}">selected</c:if>>ADMIN</option>
            </select><br><br>
            <button type="submit">Сохранить</button>
        </form>
    </c:if>

    <hr>

    <h3>Список пользователей</h3>
    <table>
        <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Role</th>
            <th>Действие</th>
        </tr>
        <c:choose>
            <c:when test="${empty users}">
                <tr><td colspan="4">Пользователей пока нет</td></tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.role}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/users?id=${user.id}">Изменить</a>
                            <form method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${user.id}">
                                <button type="submit" onclick="return confirm('Удалить пользователя?')">Удалить</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </table>

</body>
</html>