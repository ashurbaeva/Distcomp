package by.symonik.issue_service.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record CommentUpdateRequestTo(
        @NotNull(message = "ID should not be null.")
        @Positive(message = "ID should be a positive number.")
        Long id,

        @NotNull(message = "Issue-ID should not be null.")
        @Positive(message = "Issue-ID should be a positive number.")
        Long issueId,

        @NotBlank(message = "Content should not be blank.")
        @Size(min = 2, max = 2048, message = "Content size should be between 2 and 2048 chars.")
        String content
) implements Serializable {
}