package com.simplesystem.todo.service;

import com.simplesystem.todo.domain.TodoItem;
import com.simplesystem.todo.domain.TodoStatus;
import com.simplesystem.todo.exception.BusinessException;
import com.simplesystem.todo.repository.TodoRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TodoService {
  private final TodoRepository todoRepository;

  public TodoService(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }

  public TodoItem createTodo(String description, LocalDateTime dueAt) {
    TodoItem item = new TodoItem(description, dueAt);
    return todoRepository.save(item);
  }

  public TodoItem getTodo(Long id) {
    return todoRepository.findById(id)
        .orElseThrow(() -> new BusinessException("TODO_NOT_FOUND",
            HttpStatus.NOT_FOUND,
            "Todo " + id + " not found"));
  }

  public List<TodoItem> getTodos(boolean all) {
    if (all) {
      return todoRepository.findAll();
    }
    return todoRepository.findByStatus(TodoStatus.NOT_DONE);
  }

  @Transactional
  public TodoItem updateDescription(Long id, String description) {
    if (StringUtils.isBlank(description)) {
      throw new BusinessException("TODO_VALIDATION_FAILED",
          HttpStatus.BAD_REQUEST,
          "Description cannot be blank");
    }

    TodoItem todo = getTodo(id);
    ensureNotOverdue(todo);

    todo.updateDescription(description.trim());
    return todoRepository.save(todo);
  }

  @Transactional
  public TodoItem markDone(Long id) {
    TodoItem todo = getTodo(id);
    ensureNotOverdue(todo);

    todo.markDone();
    return todoRepository.save(todo);
  }

  @Transactional
  public TodoItem markNotDone(Long id) {
    TodoItem todo = getTodo(id);
    ensureNotOverdue(todo);

    todo.markNotDone();
    return todoRepository.save(todo);
  }

  // Prevent modification of todos that are already past due (even if the scheduler has not updated the status yet).
  private void ensureNotOverdue(TodoItem todo) {
    if (todo.isOverdue()) {
      throw new BusinessException("TODO_MODIFICATION_NOT_ALLOWED",
          HttpStatus.BAD_REQUEST, "Past due items cannot be modified");
    }
  }
}
