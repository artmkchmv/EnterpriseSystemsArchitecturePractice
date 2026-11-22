package library.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "authors")
public class AuthorList {

    @JsonProperty("authors")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "author")
    private List<AuthorDto> items = new ArrayList<>();

    public AuthorList() {
    }

    public AuthorList(List<AuthorDto> items) {
        this.items = items;
    }

    public List<AuthorDto> getItems() {
        return items;
    }

    public void setItems(List<AuthorDto> items) {
        this.items = items;
    }
}
