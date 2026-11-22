package library.rest;

import jakarta.validation.Valid;
import library.model.Book;
import library.service.AuthorService;
import library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/api/books",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class BookRestController {

    private final BookService bookService;
    private final AuthorService authorService;

    public BookRestController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping
    public BookList list() {
        return new BookList(null, bookService.findAll().stream().map(BookDto::from).toList());
    }

    @GetMapping("/{id}")
    public BookDto get(@PathVariable int id) {
        return BookDto.from(findBook(id));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<BookDto> create(@Valid @RequestBody BookDto dto) {
        checkAuthor(dto.getAuthorId());
        Book book = new Book();
        dto.copyTo(book);
        BookDto saved = BookDto.from(bookService.save(book, dto.getAuthorId()));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public BookDto update(@PathVariable int id, @Valid @RequestBody BookDto dto) {
        Book book = findBook(id);
        checkAuthor(dto.getAuthorId());
        dto.copyTo(book);
        return BookDto.from(bookService.save(book, dto.getAuthorId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        findBook(id);
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Book findBook(int id) {
        return bookService.find(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга не найдена"));
    }

    // автор книги должен существовать
    private void checkAuthor(int authorId) {
        if (authorService.find(authorId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Автор не найден: " + authorId);
        }
    }
}
