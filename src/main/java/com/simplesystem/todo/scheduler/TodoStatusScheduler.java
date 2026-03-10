package com.simplesystem.todo.scheduler;

import com.simplesystem.todo.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;

@Component
@Slf4j
public class TodoStatusScheduler {
  private final TodoRepository todoRepository;

  private final Clock clock;

  public TodoStatusScheduler(TodoRepository todoRepository, Clock clock) {
    this.todoRepository = todoRepository;
    this.clock = clock;
  }

  // Periodically check for overdue items and mark them as PAST_DUE.
  @Transactional
  @Scheduled(fixedRateString = "${todo.past-due-check-interval-ms:60000}")
  public void markPastDueTodos() {
    int updatedCount = todoRepository.markPastDueTodos(Instant.now(clock));

    if (updatedCount > 0) {
      log.info("Marked {} todo items as PAST_DUE", updatedCount);
    }
  }
}
