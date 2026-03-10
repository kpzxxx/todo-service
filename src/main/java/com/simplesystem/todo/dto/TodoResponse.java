package com.simplesystem.todo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.simplesystem.todo.domain.TodoItem;
import com.simplesystem.todo.domain.TodoStatus;

import java.time.LocalDateTime;

public record TodoResponse(Long id,
                           String description,
                           TodoStatus status,
                           @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")

                           LocalDateTime createdAt,
                           @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                           LocalDateTime dueAt,
                           @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                           LocalDateTime doneAt) {
  public static TodoResponse from(TodoItem item) {
    return new TodoResponse(
        item.getId(),
        item.getDescription(),
        // If todoItem's status is not DONE, and is overdue, return PAST_DUE.
        item.isOverdue() && item.getStatus() != TodoStatus.DONE ?
            TodoStatus.PAST_DUE : item.getStatus(),
        item.getCreatedAt(),
        item.getDueAt(),
        item.getDoneAt()
    );
  }
}
