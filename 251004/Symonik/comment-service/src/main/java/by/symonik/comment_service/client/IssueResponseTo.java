package by.symonik.comment_service.client;

import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record IssueResponseTo(
        Long id,
        Long editorId,
        String title,
        String content,
        LocalDateTime createdTime,
        LocalDateTime lastModifiedTime
) {
}
