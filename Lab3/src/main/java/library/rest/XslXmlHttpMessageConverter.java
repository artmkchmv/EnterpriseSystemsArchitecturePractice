package library.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.util.List;

@Component
public class XslXmlHttpMessageConverter extends MappingJackson2XmlHttpMessageConverter {

    private static final String XSL_HREF = "/xsl/library.xsl";

    public XslXmlHttpMessageConverter() {
        super(Jackson2ObjectMapperBuilder.xml().build());
        // без application/*+xml, иначе браузер получит application/xhtml+xml
        setSupportedMediaTypes(List.of(MediaType.APPLICATION_XML, MediaType.TEXT_XML));
    }

    @Override
    protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
        XMLStreamWriter writer = ((ToXmlGenerator) generator).getStaxWriter();
        try {
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"" + XSL_HREF + "\"");
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }
}
