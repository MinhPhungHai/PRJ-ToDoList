package com.minhphung.todolist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    //ID
    @Id
    @GeneratedValue
    private UUID id;

    //User input
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "urgent")
    private boolean urgent;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed")
    private boolean completed;

    @Column(name = "done_at")
    private LocalDateTime doneAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        completed = false;
        doneAt = null;
    }
}