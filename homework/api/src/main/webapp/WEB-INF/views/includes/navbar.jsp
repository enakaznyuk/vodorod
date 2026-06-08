<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<div style="margin-bottom: 16px; padding: 8px; background: #f5f5f5;">
    Вы вошли как <strong>${sessionScope.username}</strong> (${sessionScope.role})
    | <a href="${pageContext.request.contextPath}/orders">Заказы</a>
    | <a href="${pageContext.request.contextPath}/products">Товары</a>
    <c:if test="${sessionScope.role == 'ADMIN'}">
        | <a href="${pageContext.request.contextPath}/users">Пользователи</a>
    </c:if>
    | <a href="${pageContext.request.contextPath}/logout">Выйти</a>
</div>
