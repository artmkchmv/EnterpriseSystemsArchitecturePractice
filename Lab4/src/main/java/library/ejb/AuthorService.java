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
public class AuthorService {

    @PersistenceContext(unitName = "libraryPU")
    private EntityManager em;

    @EJB
    private ChangeSender changeSender;

    public List<Author> findAll() {
        return em.createQuery("SELECT a FROM Author a ORDER BY a.fullName", Author.class)
                .getResultList();
    }

    public Author find(int id) {
        return em.find(Author.class, id);
    }

    public void create(Author author) {
        em.persist(author);
        // flush нужен, чтобы получить id из базы
        em.flush();
        changeSender.authorChanged(ChangeEvent.INSERT, author);
    }

    public void update(Author author) {
        Author existing = em.find(Author.class, author.getId());
        if (existing != null) {
            existing.setFullName(author.getFullName());
            existing.setCountry(author.getCountry());
            existing.setBirthYear(author.getBirthYear());
            changeSender.authorChanged(ChangeEvent.UPDATE, existing);
        }
    }

    // вместе с автором удаляются все его книги
    public void delete(int id) {
        Author author = em.find(Author.class, id);
        if (author == null) {
            return;
        }
        List<Book> books = em.createQuery("SELECT b FROM Book b WHERE b.author = :author", Book.class)
                .setParameter("author", author)
                .getResultList();
        for (Book book : books) {
            changeSender.bookChanged(ChangeEvent.DELETE, book);
            em.remove(book);
        }
        changeSender.authorChanged(ChangeEvent.DELETE, author);
        em.remove(author);
    }
}
