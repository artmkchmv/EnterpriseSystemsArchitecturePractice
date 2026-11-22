<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Книги</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav>
    <a href="${pageContext.request.contextPath}/authors">Авторы</a>
    <a href="${pageContext.request.contextPath}/books">Книги</a>
</nav>

<h1>Книги</h1>
<a href="${pageContext.request.contextPath}/books?action=new">Добавить книгу</a>

<table>
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Жанр</th>
        <th>Год издания</th>
        <th>Страниц</th>
        <th>Автор</th>
        <th></th>
    </tr>
    <c:forEach var="b" items="${books}">
        <tr>
            <td>${b.id}</td>
            <td><c:out value="${b.title}"/></td>
            <td><c:out value="${b.genre}"/></td>
            <td>${b.publishYear}</td>
            <td>${b.pages}</td>
            <td><c:out value="${b.author.fullName}"/></td>
            <td>
                <a href="${pageContext.request.contextPath}/books?action=edit&id=${b.id}">Изменить</a>
                <form class="inline" method="post" action="${pageContext.request.contextPath}/books"
                      onsubmit="return confirm('Удалить книгу?')">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${b.id}">
                    <button type="submit">Удалить</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
