package bsuir.khanenko.modulepublisher.dto;

public class MessageResponseTo {
    private Long id;
    private Long articleId;
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public MessageResponseTo() {
    }

    public void setContent(String content) {
        this.content = content;
    }
}
