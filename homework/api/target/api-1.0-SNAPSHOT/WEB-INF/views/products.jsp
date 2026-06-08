<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head><title>Товары</title></head>
<body>
    <h1>Товары</h1>

    <form method="post">
        <input type="hidden" name="action" value="create">
        Название: <input name="name" required><br>
        Описание: <input name="description"><br>
        Цена: <input name="price" type="number" step="0.01" required><br>
        <button type="submit">Добавить товар</button>
    </form>

    <h3>Список товаров</h3>
    <table border="1">
        <tr><th>ID</th><th>Название</th><th>Описание</th><th>Цена</th><th>Действие</th></tr>
        <c:forEach var="p" items="${products}">
            <tr>
                <td>${p.id}</td>
                <td>${p.name}</td>
                <td>${p.description}</td>
                <td>${p.price}</td>
                <td>
                    <form method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${p.id}">
                        <button type="submit">Удалить</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <br>
        <a href="${pageContext.request.contextPath}/orders">Заказы</a>
</body>
</html>