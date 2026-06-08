<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Регистрация</title>
    <style>
        body { font-family: sans-serif; max-width: 400px; margin: 40px auto; }
        input { display: block; width: 100%; margin: 8px 0 16px; padding: 8px; box-sizing: border-box; }
        .error { color: red; margin-bottom: 12px; }
        .hint { color: #666; font-size: 14px; }
    </style>
</head>
<body>
    <h1>Регистрация</h1>
    <p class="hint">Новые пользователи получают роль CLIENT.</p>

    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <form method="post">
        Логин: <input type="text" name="username" value="${username}" required>
        Пароль: <input type="password" name="password" required>
        Повтор пароля: <input type="password" name="confirmPassword" required>
        <button type="submit">Зарегистрироваться</button>
    </form>

    <p>Уже есть аккаунт? <a href="${pageContext.request.contextPath}/login">Войти</a></p>
</body>
</html>
