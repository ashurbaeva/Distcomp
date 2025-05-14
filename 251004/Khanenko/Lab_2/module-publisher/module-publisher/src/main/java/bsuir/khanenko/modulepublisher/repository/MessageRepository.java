package bsuir.khanenko.modulepublisher.repository;

import bsuir.khanenko.modulepublisher.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface MessageRepository extends CrudRepository<Message, Long> {
}
