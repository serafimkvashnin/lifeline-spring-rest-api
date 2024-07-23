package com.simagames.lifelineapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@Entity
@Table(name = "task_status", schema = "main")
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate statusDate;
    private boolean workedOn;
    private String comment;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskStatus(Long id, LocalDate statusDate, boolean workedOn, String comment, Task task) {
        this.id = id;
        this.statusDate = statusDate;
        this.workedOn = workedOn;
        this.comment = comment;
        this.task = task;
    }
}
