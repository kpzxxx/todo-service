package com.simplesystem.todo.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateTodoRequest(@NotBlank String description,
                                @NotNull @Future LocalDateTime dueAt) {
}
