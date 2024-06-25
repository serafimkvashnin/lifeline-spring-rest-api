package com.simagames.lifelineapi.repository;

import com.simagames.lifelineapi.model.Task;
import com.simagames.lifelineapi.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    @Query("SELECT task_status FROM TaskStatus task_status WHERE task_status.task = :task AND task_status.statusDate = :statusDate")
    Optional<TaskStatus> findByTaskAndStatusDate(@Param("task") Task task, @Param("statusDate") LocalDate statusDate);
}
