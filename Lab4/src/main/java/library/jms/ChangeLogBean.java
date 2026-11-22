package library.jms;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import library.model.ChangeLog;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

// Записывает каждое изменение из топика в таблицу change_log
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/LibraryChanges"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Topic"),
        @ActivationConfigProperty(propertyName = "connectionFactoryLookup", propertyValue = "jms/LibraryConnectionFactory")
})
public class ChangeLogBean implements MessageListener {

    private static final Logger LOG = Logger.getLogger(ChangeLogBean.class.getName());

    @PersistenceContext(unitName = "libraryPU")
    private EntityManager em;

    @Override
    public void onMessage(Message message) {
        ChangeEvent event;
        try {
            event = ChangeEvent.fromMessage(message);
        } catch (JMSException | IllegalArgumentException e) {
            // некорректное сообщение пропускаем, чтобы оно не доставлялось повторно
            LOG.log(Level.WARNING, "Сообщение не записано в журнал: {0}", e.getMessage());
            return;
        }

        ChangeLog entry = new ChangeLog();
        entry.setChangeType(event.getChangeType());
        entry.setEntityClass(event.getEntityClass());
        entry.setEntityId(event.getEntityId());
        entry.setDetails(event.getDetails());
        entry.setChangedAt(LocalDateTime.now());
        em.persist(entry);
    }
}
