package bsuir.khanenko.modulepublisher.dto.message;

public class MessageResponseTo {
    private Long id;
    private Long articleId;
    private String content;

    // Constructor
    public MessageResponseTo(Long articleId, Long id, String content) {
        this.articleId = articleId;
        this.id = id;
        this.content = content;
    }

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

    public void setContent(String content) {
        this.content = content;
    }
}
