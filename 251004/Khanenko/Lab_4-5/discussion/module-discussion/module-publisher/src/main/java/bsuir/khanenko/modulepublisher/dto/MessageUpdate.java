package bsuir.khanenko.modulepublisher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class MessageUpdate {
    @NotNull(message = "ID should not be null.")
    @Positive(message = "ID should be a positive number.")
    private Long id;

    @NotNull(message = "articleId should not be null.")
    @Positive(message = "articleId should be a positive number.")
    private Long articleId;

    @NotBlank(message = "Content should not be blank.")
    @Size(min = 2, max = 2048, message = "Content size should be between 2 and 2048 chars.")
    private String content;

    public @NotNull(message = "ID should not be null.") @Positive(message = "ID should be a positive number.") Long getId() {
        return id;
    }

    public void setId(@NotNull(message = "ID should not be null.") @Positive(message = "ID should be a positive number.") Long id) {
        this.id = id;
    }

    public @NotNull(message = "articleId should not be null.") @Positive(message = "articleId should be a positive number.") Long getArticleId() {
        return articleId;
    }

    public void setArticleId(@NotNull(message = "articleId should not be null.") @Positive(message = "articleId should be a positive number.") Long articleId) {
        this.articleId = articleId;
    }

    public @NotBlank(message = "Content should not be blank.") @Size(min = 2, max = 2048, message = "Content size should be between 2 and 2048 chars.") String getContent() {
        return content;
    }

    public void setContent(@NotBlank(message = "Content should not be blank.") @Size(min = 2, max = 2048, message = "Content size should be between 2 and 2048 chars.") String content) {
        this.content = content;
    }
}