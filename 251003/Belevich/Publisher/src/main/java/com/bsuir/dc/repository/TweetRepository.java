package com.bsuir.dc.repository;

import com.bsuir.dc.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    boolean existsByTitle(String title);
}
