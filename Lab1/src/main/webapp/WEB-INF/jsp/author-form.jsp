<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Автор</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav>
    <a href="${pageContext.request.contextPath}/authors">Авторы</a>
    <a href="${pageContext.request.contextPath}/books">Книги</a>
</nav>

<h1>${author.id == null ? 'Новый автор' : 'Редактирование автора'}</h1>

<form method="post" action="${pageContext.request.contextPath}/authors">
    <input type="hidden" name="action" value="save">
    <input type="hidden" name="id" value="${author.id}">
    <div class="row">
        <label for="fullName">Имя</label>
        <input id="fullName" name="fullName" value="${fn:escapeXml(author.fullName)}" required maxlength="100">
    </div>
    <div class="row">
        <label for="country">Страна</label>
        <input id="country" name="country" value="${fn:escapeXml(author.country)}" maxlength="50">
    </div>
    <div class="row">
        <label for="birthYear">Год рождения</label>
        <input id="birthYear" name="birthYear" type="number" value="${author.birthYear}">
    </div>
    <button type="submit">Сохранить</button>
    <a href="${pageContext.request.contextPath}/authors">Отмена</a>
</form>
</body>
</html>
