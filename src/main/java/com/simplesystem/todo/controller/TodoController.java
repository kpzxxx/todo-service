package com.simplesystem.todo.controller;

import com.simplesystem.todo.domain.TodoItem;
import com.simplesystem.todo.dto.CreateTodoRequest;
import com.simplesystem.todo.dto.TodoResponse;
import com.simplesystem.todo.dto.UpdateDescriptionRequest;
import com.simplesystem.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

  private final TodoService todoService;

  public TodoController(TodoService todoService) {
    this.todoService = todoService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TodoResponse createTodo(@RequestBody CreateTodoRequest request) {
    TodoItem item = todoService.createTodo(request.description(), request.dueAt());
    return TodoResponse.from(item);
  }

  @GetMapping("/{id}")
  public TodoResponse getTodo(@PathVariable Long id) {
    return TodoResponse.from(todoService.getTodo(id));
  }

  @GetMapping
  public List<TodoResponse> getTodos(@RequestParam(defaultValue = "false") boolean all) {
    return todoService.getTodos(all)
        .stream()
        .map(TodoResponse::from)
        .toList();
  }

  @PatchMapping("/{id}/description")
  public TodoResponse updateDescription(@PathVariable Long id,
                                        @RequestBody UpdateDescriptionRequest request) {
    return TodoResponse.from(todoService.updateDescription(id, request.description()));
  }

  @PatchMapping("/{id}/done")
  public TodoResponse markDone(@PathVariable Long id) {
    return TodoResponse.from(todoService.markDone(id));
  }

  @PatchMapping("/{id}/not-done")
  public TodoResponse markNotDone(@PathVariable Long id) {
    return TodoResponse.from(todoService.markNotDone(id));
  }

}
