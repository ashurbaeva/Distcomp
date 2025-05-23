package com.alina.publisher.repository;

import com.alina.publisher.model.Sticker;
import com.alina.publisher.model.Tweet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TweetRepository extends Repo<Tweet>{
   @Query("SELECT s FROM Sticker s JOIN s.tweets t WHERE t.id = :tweetId")
   Set<Sticker> findStickersByTweetId(@Param("tweetId") Long tweetId);
}
