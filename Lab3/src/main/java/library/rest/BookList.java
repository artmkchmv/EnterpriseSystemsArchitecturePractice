package library.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

// Список книг: <books><book>...</book></books> или {"books": [...]}
@JacksonXmlRootElement(localName = "books")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookList {

    // заполняется, если это книги одного автора
    private AuthorDto author;

    @JsonProperty("books")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "book")
    private List<BookDto> items = new ArrayList<>();

    public BookList() {
    }

    public BookList(AuthorDto author, List<BookDto> items) {
        this.author = author;
        this.items = items;
    }

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }

    public List<BookDto> getItems() {
        return items;
    }

    public void setItems(List<BookDto> items) {
        this.items = items;
    }
}
