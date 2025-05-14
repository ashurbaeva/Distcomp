package com.example.rest.repository;

import com.example.rest.entity.Post;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends CassandraRepository<Post, String> {

    Optional<Post> findById(Long id);

    // Для поиска по country (если нужно)
    @Query("SELECT * FROM tbl_post WHERE country = ?0 ALLOW FILTERING")
    List<Post> findByCountry(String country);

    // Для поиска по content
    @Query("SELECT * FROM tbl_post WHERE content = ?0 ALLOW FILTERING")

    void deleteById(Long id);
}
