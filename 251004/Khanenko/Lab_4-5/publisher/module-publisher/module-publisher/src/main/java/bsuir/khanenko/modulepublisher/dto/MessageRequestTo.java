package bsuir.khanenko.modulepublisher.dto;

import jakarta.validation.constraints.Size;

public class MessageRequestTo {
    private Long id;
    @Size(min = 2, max = 2048, message = "Content must be between 2 and 2048 characters")
    private String content;
    private Long articleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public MessageRequestTo() {
    }

    public MessageRequestTo(Long id) {
        this.id = id;
    }
}
