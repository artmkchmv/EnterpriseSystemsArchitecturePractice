package library.jms;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.JMSConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.JMSRuntimeException;
import jakarta.jms.Topic;
import library.model.Author;
import library.model.Book;

// Отправка сообщений об изменениях сущностей в топик jms/LibraryChanges.
// Сообщение уходит в рамках текущей транзакции, т.е. только после её фиксации.
@Stateless
public class ChangeSender {

    @Inject
    @JMSConnectionFactory("jms/LibraryConnectionFactory")
    private JMSContext context;

    @Resource(lookup = "jms/LibraryChanges")
    private Topic topic;

    public void authorChanged(String changeType, Author author) {
        String details = "fullName=" + author.getFullName()
                + ", country=" + author.getCountry()
                + ", birthYear=" + author.getBirthYear();
        send(new ChangeEvent(changeType, Author.class.getName(), author.getId(), author.getFullName(), details));
    }

    public void bookChanged(String changeType, Book book) {
        String details = "title=" + book.getTitle()
                + ", genre=" + book.getGenre()
                + ", publishYear=" + book.getPublishYear()
                + ", pages=" + book.getPages()
                + ", authorId=" + book.getAuthor().getId();
        send(new ChangeEvent(changeType, Book.class.getName(), book.getId(), book.getTitle(), details));
    }

    private void send(ChangeEvent event) {
        try {
            context.createProducer().send(topic, event.toMessage(context));
        } catch (JMSException e) {
            throw new JMSRuntimeException(e.getMessage(), e.getErrorCode(), e);
        }
    }
}
