package by.symonik.issue_service.dto.editor;

import lombok.Builder;

@Builder
public record EditorResponseTo(
        Long id,
        String login,
        String password,
        String firstname,
        String lastname
) {
}

