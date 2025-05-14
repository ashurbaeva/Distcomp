package com.bsuir.dc.dto.response;

import java.io.Serializable;

public class MessageResponseTo implements Serializable {
    private long id;
    private long tweetId;
    private String content;

    public void setId(long id) { this.id = id; }
    public long getId() { return id; }

    public void setTweetId(long tweetId) { this.tweetId = tweetId; }
    public long getTweetId() { return tweetId; }

    public void setContent(String content) { this.content = content; }
    public String getContent() { return content; }
}
