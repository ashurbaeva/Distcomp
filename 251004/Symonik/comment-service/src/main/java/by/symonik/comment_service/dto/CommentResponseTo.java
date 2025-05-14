package by.symonik.comment_service.dto;

import lombok.Builder;

@Builder
public record CommentResponseTo(
        Long id,
        Long issueId,
        String content
) {
}
