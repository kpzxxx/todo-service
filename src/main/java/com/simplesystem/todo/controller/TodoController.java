package com.simplesystem.todo.controller;

import com.simplesystem.todo.domain.TodoItem;
import com.simplesystem.todo.dto.CreateTodoRequest;
import com.simplesystem.todo.dto.TodoResponse;
import com.simplesystem.todo.dto.UpdateDescriptionRequest;
import com.simplesystem.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

  private final TodoService todoService;

  private final Clock clock;

  public TodoController(TodoService todoService, Clock clock) {
    this.todoService = todoService;
    this.clock = clock;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TodoResponse createTodo(@Valid @RequestBody CreateTodoRequest request) {
    TodoItem item = todoService.createTodo(request.description(), request.dueAt());
    return TodoResponse.from(item, Instant.now(clock));
  }

  @GetMapping("/{id}")
  public TodoResponse getTodo(@PathVariable Long id) {
    return TodoResponse.from(todoService.getTodo(id), Instant.now(clock));
  }

  @GetMapping
  public List<TodoResponse> getTodos(@RequestParam(defaultValue = "false") boolean all) {
    Instant now = Instant.now(clock);
    return todoService.getTodos(all)
        .stream()
        .map(item -> TodoResponse.from(item, now))
        .toList();
  }

  @PatchMapping("/{id}/description")
  public TodoResponse updateDescription(@PathVariable Long id,
                                        @Valid @RequestBody UpdateDescriptionRequest request) {
    return TodoResponse.from(todoService.updateDescription(id, request.description()), Instant.now(clock));
  }

  @PatchMapping("/{id}/done")
  public TodoResponse markDone(@PathVariable Long id) {
    return TodoResponse.from(todoService.markDone(id), Instant.now(clock));
  }

  @PatchMapping("/{id}/not-done")
  public TodoResponse markNotDone(@PathVariable Long id) {
    return TodoResponse.from(todoService.markNotDone(id), Instant.now(clock));
  }

}
