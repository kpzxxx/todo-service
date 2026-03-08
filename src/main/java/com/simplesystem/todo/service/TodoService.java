package com.simplesystem.todo.service;

import com.simplesystem.todo.domain.TodoItem;
import com.simplesystem.todo.repository.TodoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class TodoService {
  private final TodoRepository todoRepository;

  public TodoService(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }

  public TodoItem createTodo(String description, LocalDateTime dueAt) {
    TodoItem item = new TodoItem(description, dueAt);
    return todoRepository.save(item);
  }

}
