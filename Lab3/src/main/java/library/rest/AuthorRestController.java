package library.rest;

import jakarta.validation.Valid;
import library.model.Author;
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
@RequestMapping(path = "/api/authors",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class AuthorRestController {

    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorRestController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping
    public AuthorList list() {
        return new AuthorList(authorService.findAll().stream().map(AuthorDto::from).toList());
    }

    @GetMapping("/{id}")
    public AuthorDto get(@PathVariable int id) {
        return AuthorDto.from(findAuthor(id));
    }

    @GetMapping("/{id}/books")
    public BookList books(@PathVariable int id) {
        Author author = findAuthor(id);
        return new BookList(AuthorDto.from(author),
                bookService.findByAuthor(id).stream().map(BookDto::from).toList());
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<AuthorDto> create(@Valid @RequestBody AuthorDto dto) {
        Author author = new Author();
        dto.copyTo(author);
        AuthorDto saved = AuthorDto.from(authorService.save(author));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public AuthorDto update(@PathVariable int id, @Valid @RequestBody AuthorDto dto) {
        Author author = findAuthor(id);
        dto.copyTo(author);
        return AuthorDto.from(authorService.save(author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        findAuthor(id);
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Author findAuthor(int id) {
        return authorService.find(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Автор не найден"));
    }
}
