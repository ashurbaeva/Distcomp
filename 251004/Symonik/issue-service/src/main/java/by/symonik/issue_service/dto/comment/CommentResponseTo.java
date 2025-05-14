package by.symonik.issue_service.dto.comment;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record CommentResponseTo (
        Long id,
        Long issueId,
        String content
) implements Serializable {
}
