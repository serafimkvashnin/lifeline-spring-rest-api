package com.simagames.lifelineapi.service;

import com.simagames.lifelineapi.model.Task;
import com.simagames.lifelineapi.model.TaskStatus;

import java.util.List;

public interface ITaskService {
    List<Task> getTasks(int month, int year);
    Task createTask(Task task);
    Task completeTask(Long id);
    void deleteTask(Long id);
    TaskStatus updateTaskStatus(Long id, TaskStatus status);
}
