package com.simagames.lifelineapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.simagames.lifelineapi.exception.TaskNotFoundException;
import com.simagames.lifelineapi.model.Task;
import com.simagames.lifelineapi.model.TaskStatus;
import com.simagames.lifelineapi.repository.TaskRepository;
import com.simagames.lifelineapi.repository.TaskStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskStatusRepository taskStatusRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTasks() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        List<Task> tasks = Arrays.asList(
                Task.builder().id(1L).description("Task 1").createdDate(LocalDate.now()).build(),
                Task.builder().id(2L).description("Task 2").createdDate(LocalDate.now()).build()
        );
        when(taskRepository.findTasksByCreatedDateBetween(startDate, endDate)).thenReturn(tasks);

        List<Task> result = taskService.getTasks(1, 2023);

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findTasksByCreatedDateBetween(startDate, endDate);
    }

    @Test
    public void testCreateTask() {
        Task task = Task.builder().id(1L).description("New Task").createdDate(LocalDate.now()).build();
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask("New Task");

        assertEquals("New Task", result.getDescription());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testCompleteTask() {
        Task task = Task.builder().id(1L).description("Task 1").createdDate(LocalDate.now()).build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.completeTask(1L);

        assertTrue(task.isCompleted());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testCompleteTask_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.completeTask(1L));
    }

    @Test
    public void testDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTask_NotFound() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(1L));
    }

    @Test
    public void testUpdateTaskStatus() {
        Task task = Task.builder().id(1L).description("Task 1").createdDate(LocalDate.now()).build();
        TaskStatus taskStatus = TaskStatus.builder().id(1L).task(task).statusDate(LocalDate.now()).workedOn(true).comment("Test Comment").build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskStatusRepository.findByTaskAndStatusDate(any(Task.class), any(LocalDate.class))).thenReturn(Optional.of(taskStatus));

        taskService.updateTaskStatus(1L, LocalDate.now(), true, "Test Comment");

        verify(taskStatusRepository, times(1)).save(taskStatus);
    }

    @Test
    public void testUpdateTaskStatus_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskStatus(1L, LocalDate.now(), true, "Test Comment"));
    }
}
