package bsuir.khanenko.modulepublisher.repository;

import bsuir.khanenko.modulepublisher.entity.Message;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CassandraRepository<Message, String> {
}
