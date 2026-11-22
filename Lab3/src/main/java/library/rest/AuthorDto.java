package library.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import library.model.Author;

@JacksonXmlRootElement(localName = "author")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorDto {

    private Integer id;

    @NotBlank
    @Size(max = 100)
    private String fullName;

    @Size(max = 50)
    private String country;

    private Integer birthYear;

    public static AuthorDto from(Author author) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getId());
        dto.setFullName(author.getFullName());
        dto.setCountry(author.getCountry());
        dto.setBirthYear(author.getBirthYear());
        return dto;
    }

    public void copyTo(Author author) {
        author.setFullName(fullName);
        author.setCountry(country);
        author.setBirthYear(birthYear);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }
}
