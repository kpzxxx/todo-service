package com.simplesystem.todo.repository;

import com.simplesystem.todo.domain.TodoItem;
import com.simplesystem.todo.domain.TodoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

public interface TodoRepository extends JpaRepository<TodoItem, Long> {
  List<TodoItem> findByStatus(TodoStatus status);

  // Bulk update overdue todos to PAST_DUE
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("""
       update TodoItem t
          set t.status = com.simplesystem.todo.domain.TodoStatus.PAST_DUE
        where t.status = com.simplesystem.todo.domain.TodoStatus.NOT_DONE
          and t.dueAt < :now
       """)
  @Transactional
  int markPastDueTodos(@Param("now") Instant now);
}
