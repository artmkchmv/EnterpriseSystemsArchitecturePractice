package library.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import library.model.Author;
import library.model.Book;

import java.util.List;

@Stateless
public class AuthorService {

    @PersistenceContext(unitName = "libraryPU")
    private EntityManager em;

    public List<Author> findAll() {
        return em.createQuery("SELECT a FROM Author a ORDER BY a.fullName", Author.class)
                .getResultList();
    }

    public Author find(int id) {
        return em.find(Author.class, id);
    }

    public void create(Author author) {
        em.persist(author);
    }

    public void update(Author author) {
        Author existing = em.find(Author.class, author.getId());
        if (existing != null) {
            existing.setFullName(author.getFullName());
            existing.setCountry(author.getCountry());
            existing.setBirthYear(author.getBirthYear());
        }
    }

    public void delete(int id) {
        Author author = em.find(Author.class, id);
        if (author == null) {
            return;
        }

        List<Book> books = em.createQuery("SELECT b FROM Book b WHERE b.author = :author", Book.class)
                .setParameter("author", author)
                .getResultList();

        for (Book book : books) {
            em.remove(book);
        }
        
        em.remove(author);
    }
}
