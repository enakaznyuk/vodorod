<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Заказы</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid black; padding: 8px; text-align: left; vertical-align: top; }
        form { margin-bottom: 16px; }
        .item-row { margin-bottom: 8px; }
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/views/includes/navbar.jsp"/>
<h1>Заказы</h1>

<h3>Создать заказ</h3>
<form method="post" id="create-form">
    <input type="hidden" name="action" value="create">

    <c:choose>
        <c:when test="${admin}">
            Пользователь:
            <select name="userId" required>
                <option value="">— выберите —</option>
                <c:forEach var="u" items="${users}">
                    <option value="${u.id}">${u.id} — ${u.username}</option>
                </c:forEach>
            </select>
        </c:when>
        <c:otherwise>
            <input type="hidden" name="userId" value="${currentUserId}">
            Заказ оформляется на ваш аккаунт (${sessionScope.username})
        </c:otherwise>
    </c:choose>
    <br><br>

    <div id="create-items"></div>
    <button type="button" onclick="addItemRow('create-items')">+ Добавить товар</button>
    <br><br>
    <button type="submit">Создать заказ</button>
</form>

<c:if test="${not empty order}">
<h3>Изменить заказ #${order.id}</h3>
<form method="post" id="update-form">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="id" value="${order.id}">

    <c:choose>
        <c:when test="${admin}">
            Пользователь:
            <select name="userId" required>
                <option value="">— выберите —</option>
                <c:forEach var="u" items="${users}">
                    <option value="${u.id}" <c:if test="${order.userId == u.id}">selected</c:if>>
                        ${u.id} — ${u.username}
                    </option>
                </c:forEach>
            </select>
        </c:when>
        <c:otherwise>
            <input type="hidden" name="userId" value="${currentUserId}">
            Пользователь: ${sessionScope.username}
        </c:otherwise>
    </c:choose>
    <br><br>

    <div id="update-items"></div>
    <button type="button" onclick="addItemRow('update-items')">+ Добавить товар</button>
    <br><br>
    <button type="submit">Сохранить заказ</button>
</form>
</c:if>

<h3>Список заказов</h3>

<table>
    <tr>
        <th>ID</th>
        <th>Пользователь</th>
        <th>Товары</th>
        <th>Кол-во</th>
        <th>Общая сумма</th>
        <th>Дата</th>
        <th>Действие</th>
    </tr>

    <c:choose>
        <c:when test="${empty orders}">
            <tr><td colspan="7">Заказов пока нет</td></tr>
        </c:when>
        <c:otherwise>
            <c:forEach var="o" items="${orders}">
                <tr>
                    <td>${o.id}</td>
                    <td>${o.username} (id: ${o.userId})</td>
                    <td>${o.productsText}</td>
                    <td>${o.quantitiesText}</td>
                    <td>${o.totalPrice}</td>
                    <td>${o.orderDate}</td>
                    <td>
                        <c:if test="${admin or o.userId == currentUserId}">
                            <a href="${pageContext.request.contextPath}/orders?id=${o.id}">Изменить</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</table>

<select id="product-template" style="display:none;">
    <option value="">— выберите —</option>
    <c:forEach var="p" items="${products}">
        <option value="${p.id}">${p.id} — ${p.name} (${p.price})</option>
    </c:forEach>
</select>

<script>
    function addItemRow(containerId, productId, quantity) {
        const container = document.getElementById(containerId);
        const template = document.getElementById('product-template');
        const row = document.createElement('div');
        row.className = 'item-row';

        const select = document.createElement('select');
        select.name = 'productId';
        select.required = true;
        select.innerHTML = template.innerHTML;
        if (productId) {
            select.value = String(productId);
        }

        const qty = document.createElement('input');
        qty.type = 'number';
        qty.name = 'quantity';
        qty.min = '1';
        qty.value = quantity || '1';
        qty.required = true;

        const removeBtn = document.createElement('button');
        removeBtn.type = 'button';
        removeBtn.textContent = 'Удалить';
        removeBtn.onclick = function () {
            row.remove();
        };

        row.appendChild(select);
        row.appendChild(document.createTextNode(' Кол-во: '));
        row.appendChild(qty);
        row.appendChild(document.createTextNode(' '));
        row.appendChild(removeBtn);
        container.appendChild(row);
    }

    addItemRow('create-items');

    <c:if test="${not empty order}">
        <c:forEach var="productId" items="${order.productIds}" varStatus="status">
            addItemRow('update-items', '${productId}', '${order.quantities[status.index]}');
        </c:forEach>
    </c:if>
</script>

</body>
</html>
