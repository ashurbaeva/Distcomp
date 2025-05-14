package bsuir.khanenko.modulepublisher.dto.message;

import lombok.Data;

public class MessageRequestTo {
    private String content;
    private Long articleId;

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
}
