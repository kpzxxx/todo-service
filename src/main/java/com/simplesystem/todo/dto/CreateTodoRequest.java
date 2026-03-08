package com.simplesystem.todo.dto;

import java.time.LocalDateTime;

public record CreateTodoRequest(String description, LocalDateTime dueAt) {
}
