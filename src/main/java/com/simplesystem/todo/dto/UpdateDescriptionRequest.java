package com.simplesystem.todo.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateDescriptionRequest(@NotBlank String description) {
}
