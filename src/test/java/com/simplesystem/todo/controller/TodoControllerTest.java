package com.simplesystem.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesystem.todo.dto.CreateTodoRequest;
import com.simplesystem.todo.dto.UpdateDescriptionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TodoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void createTodo_shouldReturnCreated() throws Exception {
    CreateTodoRequest request = new CreateTodoRequest(
        "Do something",
        LocalDateTime.now().plusDays(1)
    );

    mockMvc.perform(post("/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.description").value("Do something"))
        .andExpect(jsonPath("$.status").value("NOT_DONE"));
  }

  @Test
  void getTodo_shouldReturn404WhenNotFound() throws Exception {
    mockMvc.perform(get("/todos/999999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404));
  }

  @Test
  void updateDescription_shouldReturn400WhenBlank() throws Exception {
    String createResponse = mockMvc.perform(post("/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new CreateTodoRequest("Do something", LocalDateTime.now().plusDays(1))
            )))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Long id = objectMapper.readTree(createResponse).get("id").asLong();

    UpdateDescriptionRequest request = new UpdateDescriptionRequest("   ");

    mockMvc.perform(patch("/todos/{id}/description", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400));
  }

  @Test
  void markDone_shouldUpdateStatusToDone() throws Exception {
    String createResponse = mockMvc.perform(post("/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new CreateTodoRequest("Do something", LocalDateTime.now().plusDays(1))
            )))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Long id = objectMapper.readTree(createResponse).get("id").asLong();

    mockMvc.perform(patch("/todos/{id}/done", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("DONE"))
        .andExpect(jsonPath("$.doneAt").exists());
  }

  @Test
  void markNotDone_shouldClearDoneAt() throws Exception {
    String createResponse = mockMvc.perform(post("/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new CreateTodoRequest("Do something", LocalDateTime.now().plusDays(1))
            )))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Long id = objectMapper.readTree(createResponse).get("id").asLong();

    mockMvc.perform(patch("/todos/{id}/done", id))
        .andExpect(status().isOk());

    mockMvc.perform(patch("/todos/{id}/not-done", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("NOT_DONE"))
        .andExpect(jsonPath("$.doneAt").doesNotExist());
  }

  @Test
  void overdueTodo_shouldNotBeModifiable() throws Exception {
    String createResponse = mockMvc.perform(post("/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new CreateTodoRequest("Overdue todo", LocalDateTime.now().minusDays(1))
            )))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Long id = objectMapper.readTree(createResponse).get("id").asLong();

    UpdateDescriptionRequest request = new UpdateDescriptionRequest("Try to change overdue task");

    mockMvc.perform(patch("/todos/{id}/description", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Past due items cannot be modified"));
  }
}