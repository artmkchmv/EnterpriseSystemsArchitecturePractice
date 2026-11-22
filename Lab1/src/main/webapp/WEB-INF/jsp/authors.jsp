<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Авторы</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav>
    <a href="${pageContext.request.contextPath}/authors">Авторы</a>
    <a href="${pageContext.request.contextPath}/books">Книги</a>
</nav>

<h1>Авторы</h1>
<a href="${pageContext.request.contextPath}/authors?action=new">Добавить автора</a>

<table>
    <tr>
        <th>ID</th>
        <th>Имя</th>
        <th>Страна</th>
        <th>Год рождения</th>
        <th></th>
    </tr>
    <c:forEach var="a" items="${authors}">
        <tr>
            <td>${a.id}</td>
            <td><c:out value="${a.fullName}"/></td>
            <td><c:out value="${a.country}"/></td>
            <td>${a.birthYear}</td>
            <td>
                <a href="${pageContext.request.contextPath}/authors?action=edit&id=${a.id}">Изменить</a>
                <form class="inline" method="post" action="${pageContext.request.contextPath}/authors"
                      onsubmit="return confirm('Удалить автора вместе с его книгами?')">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${a.id}">
                    <button type="submit">Удалить</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
