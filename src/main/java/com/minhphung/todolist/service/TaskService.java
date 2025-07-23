package com.minhphung.todolist.service;

import com.minhphung.todolist.dto.TaskDto;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto);
    List<TaskDto> getAllTasks();
    TaskDto getTaskById(UUID taskId);
    TaskDto updateTask(UUID taskId, TaskDto taskDto);
    TaskDto markAsCompleted(UUID taskId);
    void deleteTask(UUID taskId);
    List<TaskDto> getAllCompletedTasks();
    List<TaskDto> getAllIncompleteTasks();
    List<TaskDto> getAllUrgentTasks();
    List<TaskDto> getAllNonUrgentTasks();

    boolean validateTaskTimeRangeOverlap(TaskDto taskDto);
    boolean hasValidTimeRange(TaskDto taskDto);
}
