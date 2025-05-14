package bsuir.khanenko.modulepublisher.dto.message;

import jakarta.validation.constraints.Size;
import lombok.Data;

public class MessageRequestTo {
    @Size(min = 2, max = 2048, message = "Content must be between 2 and 2048 characters")
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
