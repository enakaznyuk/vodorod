<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Товары</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid black; padding: 8px; text-align: left; }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/views/includes/navbar.jsp"/>
    <h1>Товары</h1>

    <c:if test="${admin}">
        <h3>Добавить товар</h3>
        <form method="post">
            <input type="hidden" name="action" value="create">
            Название: <input name="name" required><br><br>
            Описание: <input name="description"><br><br>
            Цена: <input name="price" type="number" step="0.01" required><br><br>
            <button type="submit">Добавить товар</button>
        </form>

        <c:if test="${not empty product}">
            <h3>Изменить товар #${product.id}</h3>
            <form method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${product.id}">
                Название: <input name="name" value="${product.name}" required><br><br>
                Описание: <input name="description" value="${product.description}"><br><br>
                Цена: <input name="price" type="number" step="0.01" value="${product.price}" required><br><br>
                <button type="submit">Сохранить</button>
            </form>
        </c:if>
    </c:if>

    <h3>Список товаров</h3>
    <table border="1">
        <tr><th>ID</th><th>Название</th><th>Описание</th><th>Цена</th>
            <c:if test="${admin}"><th>Действие</th></c:if>
        </tr>
        <c:choose>
            <c:when test="${empty products}">
                <tr><td colspan="${admin ? 5 : 4}">Товаров пока нет</td></tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="p" items="${products}">
                    <tr>
                        <td>${p.id}</td>
                        <td>${p.name}</td>
                        <td>${p.description}</td>
                        <td>${p.price}</td>
                        <c:if test="${admin}">
                            <td>
                                <a href="${pageContext.request.contextPath}/products?id=${p.id}">Изменить</a>
                                <form method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="${p.id}">
                                    <button type="submit">Удалить</button>
                                </form>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </table>
</body>
</html>
