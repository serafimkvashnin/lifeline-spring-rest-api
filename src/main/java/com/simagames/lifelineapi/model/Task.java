package com.simagames.lifelineapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@Entity
@Table(name = "task", schema = "main")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDate createdDate;
    private LocalDate completedDate;
    private boolean isCompleted;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskStatus> statuses;

    public Task(Long id, String description, LocalDate createdDate, LocalDate completedDate, boolean isCompleted, List<TaskStatus> statuses) {
        this.id = id;
        this.description = description;
        this.createdDate = createdDate;
        this.completedDate = completedDate;
        this.isCompleted = isCompleted;
        this.statuses = statuses;
    }
}
