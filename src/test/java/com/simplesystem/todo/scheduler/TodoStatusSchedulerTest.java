package com.simplesystem.todo.scheduler;

import com.simplesystem.todo.domain.TodoItem;
import com.simplesystem.todo.domain.TodoStatus;
import com.simplesystem.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class TodoStatusSchedulerTest {
  @Autowired
  private TodoRepository todoRepository;

  @Autowired
  private TodoStatusScheduler todoStatusScheduler;

  @Test
  void markPastDueTodos_shouldUpdateOverdueNotDoneTodos() {
    TodoItem todo = new TodoItem("Overdue task", LocalDateTime.now().minusDays(1));
    todo = todoRepository.save(todo);

    todoStatusScheduler.markPastDueTodos();

    TodoItem updated = todoRepository.findById(todo.getId()).orElseThrow();

    assertEquals(TodoStatus.PAST_DUE, updated.getStatus());
  }
}