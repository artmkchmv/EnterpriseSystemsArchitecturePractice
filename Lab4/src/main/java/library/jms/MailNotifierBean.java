package library.jms;

import jakarta.annotation.Resource;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

// Отправляет письмо, если имя автора или название книги содержит ключевое слово.
// Адреса и ключевое слово задаются в web.xml (notify/recipients, notify/keyword).
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/LibraryChanges"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Topic"),
        @ActivationConfigProperty(propertyName = "connectionFactoryLookup", propertyValue = "jms/LibraryConnectionFactory")
})
public class MailNotifierBean implements MessageListener {

    private static final Logger LOG = Logger.getLogger(MailNotifierBean.class.getName());

    @Resource(lookup = "mail/library")
    private Session mailSession;

    @Resource(name = "notify/recipients")
    private String recipients;

    @Resource(name = "notify/keyword")
    private String keyword;

    @Override
    public void onMessage(Message message) {
        ChangeEvent event;
        try {
            event = ChangeEvent.fromMessage(message);
        } catch (JMSException | IllegalArgumentException e) {
            LOG.log(Level.WARNING, "Некорректное сообщение: {0}", e.getMessage());
            return;
        }

        if (!matches(event)) {
            return;
        }

        try {
            sendMail(event);
        } catch (MessagingException e) {
            LOG.log(Level.SEVERE, "Не удалось отправить письмо", e);
        }
    }

    // условие оповещения: имя/название содержит ключевое слово (без учёта регистра)
    private boolean matches(ChangeEvent event) {
        return event.getName() != null
                && keyword != null && !keyword.isBlank()
                && event.getName().toLowerCase().contains(keyword.toLowerCase());
    }

    private void sendMail(ChangeEvent event) throws MessagingException {
        String entity = event.getEntityClass().substring(event.getEntityClass().lastIndexOf('.') + 1);

        MimeMessage mail = new MimeMessage(mailSession);
        mail.setFrom();
        mail.setRecipients(jakarta.mail.Message.RecipientType.TO, InternetAddress.parse(recipients));
        mail.setSubject("Библиотека: " + event.getChangeType() + " " + entity + " #" + event.getEntityId(), "UTF-8");
        mail.setText("Тип изменения: " + event.getChangeType() + "\n"
                + "Сущность: " + event.getEntityClass() + "\n"
                + "Идентификатор: " + event.getEntityId() + "\n"
                + "Данные: " + event.getDetails() + "\n", "UTF-8");
        mail.setSentDate(new Date());
        Transport.send(mail);
    }
}
