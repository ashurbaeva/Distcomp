package com.bsuir.discussion.models;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;


@Table("tbl_message")
public class Message {

    @PrimaryKeyClass
    public static class MessageKey implements Serializable {
        @PrimaryKeyColumn(name = "country", type = PrimaryKeyType.PARTITIONED)
        private String country = "Republic Of Belarus";

        @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.CLUSTERED)
        private Long id;

        @PrimaryKeyColumn(name = "tweetId", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long tweetId;

        public MessageKey() {}

        public MessageKey(String country, Long tweetId, Long id) {
            this.country = country;
            this.tweetId = tweetId;
            this.id = id;
        }

        public MessageKey(Long tweetId) { this.tweetId = tweetId; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }

        public Long getTweetId() { return tweetId; }
        public void setTweetId(Long tweetId) { this.tweetId = tweetId; }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    }


    @Column("content")
    private String content;

    @PrimaryKey
    private MessageKey key;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public MessageKey getKey() { return key; }
    public void setKey(MessageKey key) { this.key = key; }
}
