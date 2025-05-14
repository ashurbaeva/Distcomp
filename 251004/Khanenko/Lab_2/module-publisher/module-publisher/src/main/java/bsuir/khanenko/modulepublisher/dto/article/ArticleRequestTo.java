package bsuir.khanenko.modulepublisher.dto.article;

import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class ArticleRequestTo {
    @Size(min = 2, max = 64, message = "Title must be between 2 and 64 characters")
    private String title;
    @Size(min = 4, max = 2048, message = "Content must be between 4 and 2048 characters")
    private String content;
    private Long userId;
    private List<String> labels = new ArrayList<>();

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
