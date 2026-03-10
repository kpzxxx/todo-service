package com.simplesystem.todo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

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
  private Instant createdAt;

  @Column(nullable = false)
  private Instant dueAt;

  private Instant doneAt;

  public TodoItem(String description, Instant dueAt, Instant now) {
    this.description = description;
    this.status = TodoStatus.NOT_DONE;
    this.dueAt = dueAt;
    this.createdAt = now;
  }

  @PrePersist
  public void prePersist() {
    if (this.status == null) {
      this.status = TodoStatus.NOT_DONE;
    }
  }


  public void markDone(Instant now) {
    this.status = TodoStatus.DONE;
    this.doneAt = now;
  }

  public void markNotDone() {
    this.status = TodoStatus.NOT_DONE;
    this.doneAt = null;
  }

  public void updateDescription(String description) {
    this.description = description;
  }

  public boolean isOverdue(Instant now) {
    return dueAt != null
        && dueAt.isBefore(now);
  }
}
