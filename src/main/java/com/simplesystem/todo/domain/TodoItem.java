package com.simplesystem.todo.domain;

import com.simplesystem.todo.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "todo_item")
public class TodoItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TodoStatus status;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime dueAt;

  private LocalDateTime doneAt;

  public TodoItem(String description, LocalDateTime dueAt) {

    if (StringUtils.isBlank(description)) {
      throw new BusinessException( "TODO_VALIDATION_FAILED",
          HttpStatus.BAD_REQUEST, "Description cannot be blank");
    }

    this.description = description;
    this.status = TodoStatus.NOT_DONE;
    this.createdAt = LocalDateTime.now();
    this.dueAt = dueAt;
  }

  @PrePersist
  public void prePersist() {
    if (this.createdAt == null) {
      this.createdAt = LocalDateTime.now();
    }
    if (this.status == null) {
      this.status = TodoStatus.NOT_DONE;
    }
  }


  public void markDone() {
    this.status = TodoStatus.DONE;
    this.doneAt = LocalDateTime.now();
  }

  public void markNotDone() {
    this.status = TodoStatus.NOT_DONE;
    this.doneAt = null;
  }

  public void updateDescription(String description) {
    this.description = description;
  }

  public boolean isOverdue() {
    return dueAt != null
        && dueAt.isBefore(LocalDateTime.now());
  }
}
