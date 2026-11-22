package library.jms;

import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;

// Изменение сущности. Передаётся как MapMessage с полями:
// changeType, entityClass, entityId, name, details
public class ChangeEvent {

    public static final String INSERT = "INSERT";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";

    private final String changeType;
    private final String entityClass;
    private final int entityId;
    private final String name;
    private final String details;

    public ChangeEvent(String changeType, String entityClass, int entityId, String name, String details) {
        this.changeType = changeType;
        this.entityClass = entityClass;
        this.entityId = entityId;
        this.name = name;
        this.details = details;
    }

    public MapMessage toMessage(JMSContext context) throws JMSException {
        MapMessage message = context.createMapMessage();
        message.setString("changeType", changeType);
        message.setString("entityClass", entityClass);
        message.setInt("entityId", entityId);
        message.setString("name", name);
        message.setString("details", details);
        return message;
    }

    // проверка типа и структуры полученного сообщения
    public static ChangeEvent fromMessage(Message message) throws JMSException {
        if (!(message instanceof MapMessage)) {
            throw new IllegalArgumentException("Ожидалось сообщение типа MapMessage");
        }
        MapMessage map = (MapMessage) message;

        String[] fields = {"changeType", "entityClass", "entityId", "name", "details"};
        for (String field : fields) {
            if (!map.itemExists(field)) {
                throw new IllegalArgumentException("В сообщении нет поля " + field);
            }
        }

        String changeType = map.getString("changeType");
        if (!INSERT.equals(changeType) && !UPDATE.equals(changeType) && !DELETE.equals(changeType)) {
            throw new IllegalArgumentException("Неизвестный тип изменения: " + changeType);
        }
        String entityClass = map.getString("entityClass");
        if (entityClass == null || entityClass.isBlank()) {
            throw new IllegalArgumentException("Не указан класс сущности");
        }
        return new ChangeEvent(changeType, entityClass, map.getInt("entityId"),
                map.getString("name"), map.getString("details"));
    }

    public String getChangeType() {
        return changeType;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public int getEntityId() {
        return entityId;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }
}
