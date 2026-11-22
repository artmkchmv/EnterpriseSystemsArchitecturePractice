package library.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import library.ejb.AuthorService;
import library.ejb.BookService;
import library.model.Book;

import java.io.IOException;

@WebServlet("/books")
public class BookServlet extends HttpServlet {

    @EJB
    private BookService bookService;

    @EJB
    private AuthorService authorService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("new".equals(action) || "edit".equals(action)) {
            Book book = new Book();
            if ("edit".equals(action)) {
                book = bookService.find(Integer.parseInt(req.getParameter("id")));
                if (book == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Книга не найдена");
                    return;
                }
            }
            req.setAttribute("book", book);
            req.setAttribute("authors", authorService.findAll());
            req.getRequestDispatcher("/WEB-INF/jsp/book-form.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("books", bookService.findAll());
        req.getRequestDispatcher("/WEB-INF/jsp/books.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            bookService.delete(Integer.parseInt(req.getParameter("id")));
        } else {
            Book book = new Book();
            Integer authorId;
            try {
                book.setId(FormUtils.toInt(req.getParameter("id")));
                book.setTitle(FormUtils.trim(req.getParameter("title")));
                book.setGenre(FormUtils.trim(req.getParameter("genre")));
                book.setPublishYear(FormUtils.toInt(req.getParameter("publishYear")));
                book.setPages(FormUtils.toInt(req.getParameter("pages")));
                authorId = FormUtils.toInt(req.getParameter("authorId"));
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректное число");
                return;
            }
            if (book.getTitle() == null || authorId == null || authorService.find(authorId) == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указано название или автор");
                return;
            }

            if (book.getId() == null) {
                bookService.create(book, authorId);
            } else {
                bookService.update(book, authorId);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/books");
    }
}
