<?xml version="1.0" encoding="UTF-8"?>
<!-- Преобразование XML-ответов REST API в HTML-страницы -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <!-- общий каркас страницы -->
    <xsl:template name="page">
        <xsl:param name="title"/>
        <xsl:param name="content"/>
        <html>
            <head>
                <title><xsl:value-of select="$title"/></title>
                <link rel="stylesheet" href="/css/style.css"/>
            </head>
            <body>
                <nav>
                    <a href="/api/authors">Авторы</a>
                    <a href="/api/books">Книги</a>
                </nav>
                <h1><xsl:value-of select="$title"/></h1>
                <xsl:copy-of select="$content"/>
            </body>
        </html>
    </xsl:template>

    <!-- список авторов -->
    <xsl:template match="/authors">
        <xsl:call-template name="page">
            <xsl:with-param name="title">Авторы</xsl:with-param>
            <xsl:with-param name="content">
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Имя</th>
                        <th>Страна</th>
                        <th>Год рождения</th>
                        <th></th>
                    </tr>
                    <xsl:for-each select="author">
                        <tr>
                            <td><xsl:value-of select="id"/></td>
                            <td><a href="/api/authors/{id}"><xsl:value-of select="fullName"/></a></td>
                            <td><xsl:value-of select="country"/></td>
                            <td><xsl:value-of select="birthYear"/></td>
                            <td><a href="/api/authors/{id}/books">Книги</a></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <!-- один автор -->
    <xsl:template match="/author">
        <xsl:call-template name="page">
            <xsl:with-param name="title" select="fullName"/>
            <xsl:with-param name="content">
                <table>
                    <tr><th>ID</th><td><xsl:value-of select="id"/></td></tr>
                    <tr><th>Имя</th><td><xsl:value-of select="fullName"/></td></tr>
                    <tr><th>Страна</th><td><xsl:value-of select="country"/></td></tr>
                    <tr><th>Год рождения</th><td><xsl:value-of select="birthYear"/></td></tr>
                </table>
                <p>
                    <a href="/api/authors/{id}/books">Книги автора</a>
                </p>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <!-- список книг (всех или одного автора) -->
    <xsl:template match="/books">
        <xsl:call-template name="page">
            <xsl:with-param name="title">
                <xsl:text>Книги</xsl:text>
                <xsl:if test="author">
                    <xsl:text> автора </xsl:text>
                    <xsl:value-of select="author/fullName"/>
                </xsl:if>
            </xsl:with-param>
            <xsl:with-param name="content">
                <xsl:if test="author">
                    <p><a href="/api/authors/{author/id}">Об авторе</a></p>
                </xsl:if>
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Название</th>
                        <th>Жанр</th>
                        <th>Год издания</th>
                        <th>Страниц</th>
                        <th>Автор</th>
                    </tr>
                    <xsl:for-each select="book">
                        <tr>
                            <td><xsl:value-of select="id"/></td>
                            <td><a href="/api/books/{id}"><xsl:value-of select="title"/></a></td>
                            <td><xsl:value-of select="genre"/></td>
                            <td><xsl:value-of select="publishYear"/></td>
                            <td><xsl:value-of select="pages"/></td>
                            <td><a href="/api/authors/{authorId}"><xsl:value-of select="authorName"/></a></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <!-- одна книга -->
    <xsl:template match="/book">
        <xsl:call-template name="page">
            <xsl:with-param name="title" select="title"/>
            <xsl:with-param name="content">
                <table>
                    <tr><th>ID</th><td><xsl:value-of select="id"/></td></tr>
                    <tr><th>Название</th><td><xsl:value-of select="title"/></td></tr>
                    <tr><th>Жанр</th><td><xsl:value-of select="genre"/></td></tr>
                    <tr><th>Год издания</th><td><xsl:value-of select="publishYear"/></td></tr>
                    <tr><th>Страниц</th><td><xsl:value-of select="pages"/></td></tr>
                    <tr>
                        <th>Автор</th>
                        <td><a href="/api/authors/{authorId}"><xsl:value-of select="authorName"/></a></td>
                    </tr>
                </table>
                <p>
                    <a href="/api/authors/{authorId}/books">Другие книги автора</a>
                </p>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
</xsl:stylesheet>
