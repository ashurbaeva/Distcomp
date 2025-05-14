package com.bsuir.dc.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TweetResponseTo implements Serializable {
    private long id;
    private long editorId;
    private String title;
    private String content;
    private Date created;
    private Date modified;
    private List<String> tags = new ArrayList<>();

    public void setId(long id) { this.id = id; }
    public long getId() { return id; }

    public void setEditorId(long editorId) { this.editorId = editorId; }
    public long getEditorId() { return editorId; }

    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return title; }

    public void setContent(String content) { this.content = content; }
    public String getContent() { return content; }

    public void setCreated(Date created) { this.created = created; }
    public Date getCreated() { return created; }

    public void setModified(Date modified) { this.modified = modified; }
    public Date getModified() { return modified; }

    public void setTags(List<String> tags) { this.tags = tags; }
    public List<String> getTags() { return tags; }
}
