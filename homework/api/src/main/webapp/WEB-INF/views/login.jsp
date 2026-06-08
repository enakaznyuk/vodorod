<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Вход</title>
    <style>
        body { font-family: sans-serif; max-width: 400px; margin: 40px auto; }
        input { display: block; width: 100%; margin: 8px 0 16px; padding: 8px; box-sizing: border-box; }
        .error { color: red; margin-bottom: 12px; }
    </style>
</head>
<body>
    <h1>Вход</h1>

    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <form method="post">
        Логин: <input type="text" name="username" required>
        Пароль: <input type="password" name="password" required>
        <button type="submit">Войти</button>
    </form>

    <p>Нет аккаунта? <a href="${pageContext.request.contextPath}/register">Регистрация</a></p>
</body>
</html>
