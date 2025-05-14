package bsuir.khanenko.modulepublisher.repository;

import bsuir.khanenko.modulepublisher.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MessageRepositoryIml implements MessageRepository {

    private final Map<Long, Message> messages = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public Message create(Message message) {
        message.setId(nextId++);
        messages.put(message.getId(), message);
        return message;
    }

    @Override
    public Message update(Message updatedMessage) {
        if (!messages.containsKey(updatedMessage.getId())) {
            throw new IllegalArgumentException("Message with ID " + updatedMessage.getId() + " not found");
        }
        messages.put(updatedMessage.getId(), updatedMessage);
        return updatedMessage;
    }

    @Override
    public void deleteById(Long id) {
        messages.remove(id);

    }

    @Override
    public List<Message> findAll() {
        return messages.values().stream().toList();
    }

    @Override
    public Optional<Message> findById(Long id) {
        return Optional.ofNullable(messages.get(id));
    }
}
