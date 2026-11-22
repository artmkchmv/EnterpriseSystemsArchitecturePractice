<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Книга</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav>
    <a href="${pageContext.request.contextPath}/authors">Авторы</a>
    <a href="${pageContext.request.contextPath}/books">Книги</a>
</nav>

<h1>${book.id == null ? 'Новая книга' : 'Редактирование книги'}</h1>

<form method="post" action="${pageContext.request.contextPath}/books">
    <input type="hidden" name="action" value="save">
    <input type="hidden" name="id" value="${book.id}">
    <div class="row">
        <label for="title">Название</label>
        <input id="title" name="title" value="${fn:escapeXml(book.title)}" required maxlength="200">
    </div>
    <div class="row">
        <label for="genre">Жанр</label>
        <input id="genre" name="genre" value="${fn:escapeXml(book.genre)}" maxlength="50">
    </div>
    <div class="row">
        <label for="publishYear">Год издания</label>
        <input id="publishYear" name="publishYear" type="number" value="${book.publishYear}">
    </div>
    <div class="row">
        <label for="pages">Страниц</label>
        <input id="pages" name="pages" type="number" min="1" value="${book.pages}">
    </div>
    <div class="row">
        <label for="authorId">Автор</label>
        <select id="authorId" name="authorId" required>
            <c:forEach var="a" items="${authors}">
                <option value="${a.id}" ${book.author != null && book.author.id == a.id ? 'selected' : ''}>
                    <c:out value="${a.fullName}"/>
                </option>
            </c:forEach>
        </select>
    </div>
    <button type="submit">Сохранить</button>
    <a href="${pageContext.request.contextPath}/books">Отмена</a>
</form>
</body>
</html>
