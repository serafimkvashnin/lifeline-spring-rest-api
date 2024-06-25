package com.simagames.lifelineapi.service;

import com.simagames.lifelineapi.exception.TaskNotFoundException;
import com.simagames.lifelineapi.model.Task;
import com.simagames.lifelineapi.model.TaskStatus;
import com.simagames.lifelineapi.repository.TaskRepository;
import com.simagames.lifelineapi.repository.TaskStatusRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService {
    private final TaskRepository taskRepository;

    private final TaskStatusRepository taskStatusRepository;

    public TaskService(TaskRepository taskRepository, TaskStatusRepository taskStatusRepository) {
        this.taskRepository = taskRepository;
        this.taskStatusRepository = taskStatusRepository;
    }

    @Override
    public List<Task> getTasks(int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return taskRepository.findTasksByCreatedDateBetween(startDate, endDate);
    }

    @Override
    public Task createTask(Task task) {
        task.setCreatedDate(LocalDate.now());
        return taskRepository.save(task);
    }

    @Override
    public Task completeTask(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setCompleted(true);
            task.setCompletedDate(LocalDate.now());
            return taskRepository.save(task);
        }
        throw new TaskNotFoundException("Task not found with id " + id);
    }

    @Override
    public void deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new TaskNotFoundException("Task not found with id " + id);
        }
    }

    @Override
    public TaskStatus updateTaskStatus(Long id, TaskStatus status) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            status.setTask(task);
            return taskStatusRepository.save(status);
        }
        throw new TaskNotFoundException("Task not found with id " + id);
    }
}
