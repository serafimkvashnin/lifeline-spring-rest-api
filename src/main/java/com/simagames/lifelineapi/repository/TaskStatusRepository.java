package com.simagames.lifelineapi.repository;

import com.simagames.lifelineapi.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
}
