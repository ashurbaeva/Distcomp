package by.symonik.issue_service.dto.label;

import lombok.Builder;

@Builder
public record LabelResponseTo(
        Long id,
        String name
) {
}
