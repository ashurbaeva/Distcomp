package com.bsuir.dc.dto.request;

import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class TweetRequestTo {
    private long id;
    private long editorId;
    private List<String> tags = new ArrayList<>();

    @Size(min = 2, max = 64, message = "Title size must be between 2..64 characters")
    private String title;

    @Size(min = 4, max = 2048, message = "Content size must be between 2..64 characters")
    private String content;

    public void setId(long id) { this.id = id; }
    public long getId() { return id; }

    public void setEditorId(long editorId) { this.editorId = editorId; }
    public long getEditorId() { return editorId; }

    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return title; }

    public void setContent(String content) { this.content = content; }
    public String getContent() { return content; }

    public void setTags(List<String> tags) { this.tags = tags; }
    public List<String> getTags() { return tags; }
}
