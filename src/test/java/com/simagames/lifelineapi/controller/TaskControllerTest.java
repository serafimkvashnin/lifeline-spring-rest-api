package com.simagames.lifelineapi.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.simagames.lifelineapi.exception.TaskNotFoundException;
import com.simagames.lifelineapi.model.Task;
import com.simagames.lifelineapi.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    public void testGetTasks() throws Exception {
        List<Task> tasks = Arrays.asList(
                Task.builder().id(1L).description("Task 1").createdDate(LocalDate.now()).build(),
                Task.builder().id(2L).description("Task 2").createdDate(LocalDate.now()).build()
        );
        when(taskService.getTasks(1, 2023)).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks?month=1&year=2023"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Task 1"))
                .andExpect(jsonPath("$[1].description").value("Task 2"));

        verify(taskService, times(1)).getTasks(1, 2023);
    }

    @Test
    public void testCreateTask() throws Exception {
        Task task = Task.builder().id(1L).description("New Task").createdDate(LocalDate.now()).build();
        when(taskService.createTask("New Task")).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .param("description", "New Task")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("New Task"));

        verify(taskService, times(1)).createTask("New Task");
    }

    @Test
    public void testCompleteTask() throws Exception {
        doNothing().when(taskService).completeTask(1L);

        mockMvc.perform(post("/api/tasks/1/complete"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).completeTask(1L);
    }

    @Test
    public void testCompleteTask_NotFound() throws Exception {
        doThrow(new TaskNotFoundException("Task not found")).when(taskService).completeTask(1L);

        mockMvc.perform(post("/api/tasks/1/complete"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).completeTask(1L);
    }

    @Test
    public void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    public void testDeleteTask_NotFound() throws Exception {
        doThrow(new TaskNotFoundException("Task not found")).when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    public void testUpdateTaskStatus() throws Exception {
        doNothing().when(taskService).updateTaskStatus(eq(1L), any(LocalDate.class), eq(true), eq("Test Comment"));

        mockMvc.perform(post("/api/tasks/1/status")
                        .param("date", "2023-01-01")
                        .param("workedOn", "true")
                        .param("comment", "Test Comment"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).updateTaskStatus(eq(1L), any(LocalDate.class), eq(true), eq("Test Comment"));
    }

    @Test
    public void testUpdateTaskStatus_NotFound() throws Exception {
        doThrow(new TaskNotFoundException("Task not found")).when(taskService).updateTaskStatus(eq(1L), any(LocalDate.class), eq(true), eq("Test Comment"));

        mockMvc.perform(post("/api/tasks/1/status")
                        .param("date", "2023-01-01")
                        .param("workedOn", "true")
                        .param("comment", "Test Comment"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).updateTaskStatus(eq(1L), any(LocalDate.class), eq(true), eq("Test Comment"));
    }
}
