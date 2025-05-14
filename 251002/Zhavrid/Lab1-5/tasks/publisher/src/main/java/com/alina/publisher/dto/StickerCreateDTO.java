package com.alina.publisher.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record StickerCreateDTO(@NotNull @Length(min = 2, max = 32) String name) {
}
