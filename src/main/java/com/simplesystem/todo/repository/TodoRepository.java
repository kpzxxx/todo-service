package com.simplesystem.todo.repository;

import com.simplesystem.todo.domain.TodoItem;
import com.simplesystem.todo.domain.TodoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoItem, Long> {
  List<TodoItem> findByStatusNot(TodoStatus status);
}
