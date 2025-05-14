package com.bsuir.dc.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tbl_tweet")
public class Tweet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne()
    @JoinColumn(name = "editorId", referencedColumnName = "id")
    private Editor editor;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created")
    private Date created;

    @Column(name = "modified")
    private Date modified;

    @ManyToMany(mappedBy = "tweets", cascade = CascadeType.REMOVE)
    private List<Tag> tags = new ArrayList<>();

    public void setId(long id) { this.id = id;}
    public long getId() { return id; }

    public void setEditor(Editor editor) { this.editor = editor; }
    public Editor getEditor() { return editor; }

    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return title; }

    public void setContent(String content) { this.content = content; }
    public String getContent() { return content; }

    public void setTags(List<Tag> tags) { this.tags = tags; }
    public List<Tag> getTags() { return tags; }

    public void setCreated(Date created) { this.created = created; }
    public Date getCreated() { return created; }

    public void setModified(Date modified) { this.modified = modified; }
    public Date getModified() { return modified; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tweet tweet = (Tweet) o;
        return id == tweet.id && editor.equals(tweet.editor) && title.equals(tweet.title) && content.equals(tweet.content) && created.equals(tweet.created) && modified.equals(tweet.modified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, editor, title, content, created, modified);
    }
}
