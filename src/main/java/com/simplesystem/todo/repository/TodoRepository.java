package com.simplesystem.todo.repository;

import com.simplesystem.todo.domain.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<TodoItem, Long> {
}
