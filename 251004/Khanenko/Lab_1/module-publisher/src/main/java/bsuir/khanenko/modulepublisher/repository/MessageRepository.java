package bsuir.khanenko.modulepublisher.repository;

import bsuir.khanenko.modulepublisher.entity.Message;
import java.util.List;
import java.util.Optional;

public interface MessageRepository {
    Message create(Message message);
    Message update(Message updatedMessage);
    void deleteById(Long id);
    List<Message> findAll();
    Optional<Message> findById(Long id);
}
