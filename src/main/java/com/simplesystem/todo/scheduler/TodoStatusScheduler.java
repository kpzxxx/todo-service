package com.simplesystem.todo.scheduler;

import com.simplesystem.todo.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class TodoStatusScheduler {
  private final TodoRepository todoRepository;

  public TodoStatusScheduler(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }

  // Periodically check for overdue items and mark them as PAST_DUE.
  @Transactional
  @Scheduled(fixedRateString = "${todo.past-due-check-interval-ms:60000}")
  public void markPastDueTodos() {
    int updatedCount = todoRepository.markPastDueTodos(LocalDateTime.now());

    if (updatedCount > 0) {
      log.info("Marked {} todo items as PAST_DUE", updatedCount);
    }
  }
}
