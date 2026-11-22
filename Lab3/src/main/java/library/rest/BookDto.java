package library.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import library.model.Book;

// Представление книги в REST API (JSON и XML)
@JacksonXmlRootElement(localName = "book")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDto {

    private Integer id;

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 50)
    private String genre;

    private Integer publishYear;

    @Positive
    private Integer pages;

    @NotNull
    private Integer authorId;

    // только для вывода, при записи игнорируется
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String authorName;

    public static BookDto from(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setGenre(book.getGenre());
        dto.setPublishYear(book.getPublishYear());
        dto.setPages(book.getPages());
        dto.setAuthorId(book.getAuthor().getId());
        dto.setAuthorName(book.getAuthor().getFullName());
        return dto;
    }

    public void copyTo(Book book) {
        book.setTitle(title);
        book.setGenre(genre);
        book.setPublishYear(publishYear);
        book.setPages(pages);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
