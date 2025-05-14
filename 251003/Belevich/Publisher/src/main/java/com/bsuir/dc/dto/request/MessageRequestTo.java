package com.bsuir.dc.dto.request;

import jakarta.validation.constraints.Size;

public class MessageRequestTo {
    private long id;
    private long tweetId;

    @Size(min = 2, max = 2048, message = "Content size must be between 2..64 characters")
    private String content;

    public void setId(long id) { this.id = id; }
    public Long getId() { return id; }

    public void setTweetId(long tweetId) { this.tweetId = tweetId; }
    public long getTweetId() { return tweetId; }

    public void setContent(String content) { this.content = content; }
    public String getContent() { return content; }

    public MessageRequestTo(Long id) { this.id = id; }

    public MessageRequestTo() {}
}
