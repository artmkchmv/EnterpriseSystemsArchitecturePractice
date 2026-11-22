package library.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import library.jms.ChangeEvent;
import library.jms.ChangeSender;
import library.model.Author;
import library.model.Book;

import java.util.List;

@Stateless
public class BookService {

    @PersistenceContext(unitName = "libraryPU")
    private EntityManager em;

    @EJB
    private ChangeSender changeSender;

    public List<Book> findAll() {
        return em.createQuery("SELECT b FROM Book b JOIN FETCH b.author ORDER BY b.title", Book.class)
                .getResultList();
    }

    public Book find(int id) {
        return em.find(Book.class, id);
    }

    public void create(Book book, int authorId) {
        book.setAuthor(em.find(Author.class, authorId));
        em.persist(book);
        em.flush();
        changeSender.bookChanged(ChangeEvent.INSERT, book);
    }

    public void update(Book book, int authorId) {
        book.setAuthor(em.find(Author.class, authorId));
        Book merged = em.merge(book);
        changeSender.bookChanged(ChangeEvent.UPDATE, merged);
    }

    public void delete(int id) {
        Book book = em.find(Book.class, id);
        if (book != null) {
            changeSender.bookChanged(ChangeEvent.DELETE, book);
            em.remove(book);
        }
    }
}
