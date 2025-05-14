package com.bsuir.discussion.repositories;

import com.bsuir.discussion.models.Message;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessagesRepository extends CrudRepository<Message, Message.MessageKey> {
    @Query("SELECT * FROM tbl_message WHERE country = :country AND id = :id")
    Optional<Message> findByCountryAndId(@Param("country") String country, @Param("id") Long id);

    @Query("DELETE FROM tbl_message WHERE country = :country AND id = :id")
    void deleteByCountryAndId(@Param("country") String country, @Param("id") Long id);
}
