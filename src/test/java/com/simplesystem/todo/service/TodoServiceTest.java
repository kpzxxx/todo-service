package com.simplesystem.todo.service;

import com.simplesystem.todo.domain.TodoItem;
import com.simplesystem.todo.domain.TodoStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TodoServiceTest {

  @Autowired
  private TodoService todoService;

  @Test
  void createTodo_shouldInitializeDefaultFields() {
    TodoItem todo = todoService.createTodo("Test create", LocalDateTime.now().plusDays(1));

    assertNotNull(todo.getId());
    assertEquals(TodoStatus.NOT_DONE, todo.getStatus());
    assertNotNull(todo.getCreatedAt());
    assertNull(todo.getDoneAt());
  }

}