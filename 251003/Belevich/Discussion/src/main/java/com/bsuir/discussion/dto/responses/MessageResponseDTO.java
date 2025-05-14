package com.bsuir.discussion.dto.responses;

public class MessageResponseDTO {
    private long id;

    private long tweetId;

    private String content;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getTweetId() { return tweetId; }
    public void setTweetId(long tweetId) { this.tweetId = tweetId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}