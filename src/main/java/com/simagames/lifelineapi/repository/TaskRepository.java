package com.simagames.lifelineapi.repository;

import com.simagames.lifelineapi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT task FROM Task task WHERE task.createdDate BETWEEN :startDate AND :endDate")
    List<Task> findTasksByCreatedDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
