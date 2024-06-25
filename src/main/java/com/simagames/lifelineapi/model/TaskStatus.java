package com.simagames.lifelineapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "task_status", schema = "main")
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate statusDate;
    private boolean workedOn;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
