package com.simplesystem.todo.dto;

import com.simplesystem.todo.domain.TodoItem;
import com.simplesystem.todo.domain.TodoStatus;

import java.time.Instant;

public record TodoResponse(Long id,
                           String description,
                           TodoStatus status,

                           Instant createdAt,
                           Instant dueAt,
                           Instant doneAt) {
  public static TodoResponse from(TodoItem item, Instant now) {
    return new TodoResponse(
        item.getId(),
        item.getDescription(),
        // If the item is not DONE and overdue, return PAST_DUE.
        item.isOverdue(now) && item.getStatus() != TodoStatus.DONE ?
            TodoStatus.PAST_DUE : item.getStatus(),
        item.getCreatedAt(),
        item.getDueAt(),
        item.getDoneAt()
    );
  }
}
