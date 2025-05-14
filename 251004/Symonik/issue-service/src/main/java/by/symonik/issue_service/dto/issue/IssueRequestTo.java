package by.symonik.issue_service.dto.issue;

import by.symonik.issue_service.dto.label.LabelRequestTo;
import by.symonik.issue_service.util.LabelRequestToDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record IssueRequestTo(
        @NotNull(message = "Editor-ID should not be null.")
        @Positive(message = "Editor-ID should be a positive number.")
        Long editorId,

        @NotBlank(message = "Title should not be blank.")
        @Size(min = 2, max = 64, message = "Title size should be between 2 and 64 chars.")
        String title,

        @NotBlank(message = "Content should not be blank.")
        @Size(min = 4, max = 2048, message = "Content size should be between 4 and 2048 chars.")
        String content,

        @JsonDeserialize(using = LabelRequestToDeserializer.class)
        List<LabelRequestTo> labels
) {
}
