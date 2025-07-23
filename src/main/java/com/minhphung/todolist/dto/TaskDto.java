package com.minhphung.todolist.dto;

import jakarta.persistence.Column;
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
public class TaskDto {

    //ID
    private UUID id;

    //User input
    private String title;
    private String description;
    private LocalDateTime deadline;
    private boolean urgent;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    //Not a user input
    private LocalDateTime createdAt;

    //Change after
    private boolean completed;
    private LocalDateTime doneAt;
}
