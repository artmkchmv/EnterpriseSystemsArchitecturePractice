package library.service;

import library.model.Author;
import library.model.Book;
import library.repository.AuthorRepository;
import library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAllWithAuthor();
    }

    public Optional<Book> find(int id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public Book save(Book book, int authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Автор не найден: " + authorId));
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }
}
