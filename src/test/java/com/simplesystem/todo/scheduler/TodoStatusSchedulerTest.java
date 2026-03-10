package com.simplesystem.todo.scheduler;

import com.simplesystem.todo.domain.TodoItem;
import com.simplesystem.todo.domain.TodoStatus;
import com.simplesystem.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class TodoStatusSchedulerTest {
  @Autowired
  private TodoRepository todoRepository;

  @Autowired
  private TodoStatusScheduler todoStatusScheduler;

  @Autowired
  private Clock clock;

  @Test
  void markPastDueTodos_shouldUpdateOverdueNotDoneTodos() {
    TodoItem todo = new TodoItem("Overdue task",
        Instant.now(clock).minus(1, ChronoUnit.DAYS), Instant.now(clock));
    todo = todoRepository.save(todo);

    todoStatusScheduler.markPastDueTodos();

    TodoItem updated = todoRepository.findById(todo.getId()).orElseThrow();

    assertEquals(TodoStatus.PAST_DUE, updated.getStatus());
  }
}