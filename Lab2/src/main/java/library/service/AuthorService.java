package library.service;

import library.model.Author;
import library.repository.AuthorRepository;
import library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public List<Author> findAll() {
        return authorRepository.findAllByOrderByFullNameAsc();
    }

    public Optional<Author> find(int id) {
        return authorRepository.findById(id);
    }

    @Transactional
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteAll(bookRepository.findByAuthorId(id));
        authorRepository.deleteById(id);
    }
}
