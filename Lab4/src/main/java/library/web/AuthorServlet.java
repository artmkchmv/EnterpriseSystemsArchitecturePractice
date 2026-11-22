package library.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import library.ejb.AuthorService;
import library.model.Author;

import java.io.IOException;

@WebServlet("/authors")
public class AuthorServlet extends HttpServlet {

    @EJB
    private AuthorService authorService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("new".equals(action) || "edit".equals(action)) {
            Author author = new Author();
            if ("edit".equals(action)) {
                author = authorService.find(Integer.parseInt(req.getParameter("id")));
                if (author == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Автор не найден");
                    return;
                }
            }
            req.setAttribute("author", author);
            req.getRequestDispatcher("/WEB-INF/jsp/author-form.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("authors", authorService.findAll());
        req.getRequestDispatcher("/WEB-INF/jsp/authors.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            authorService.delete(Integer.parseInt(req.getParameter("id")));
        } else {
            Author author = new Author();
            try {
                author.setId(FormUtils.toInt(req.getParameter("id")));
                author.setFullName(FormUtils.trim(req.getParameter("fullName")));
                author.setCountry(FormUtils.trim(req.getParameter("country")));
                author.setBirthYear(FormUtils.toInt(req.getParameter("birthYear")));
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректное число");
                return;
            }
            if (author.getFullName() == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указано имя автора");
                return;
            }

            if (author.getId() == null) {
                authorService.create(author);
            } else {
                authorService.update(author);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/authors");
    }
}
