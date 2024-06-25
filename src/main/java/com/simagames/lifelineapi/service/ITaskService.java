package com.simagames.lifelineapi.service;

import com.simagames.lifelineapi.model.Task;

import java.time.LocalDate;
import java.util.List;

public interface ITaskService {
    List<Task> getTasks(int month, int year);
    Task createTask(String description);
    void completeTask(Long id);
    void deleteTask(Long id);
    void updateTaskStatus(Long id, LocalDate statusDate, boolean workedOn, String comment);
}
