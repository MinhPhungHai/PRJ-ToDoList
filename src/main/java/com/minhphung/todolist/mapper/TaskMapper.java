package com.minhphung.todolist.mapper;

import com.minhphung.todolist.dto.TaskDto;
import com.minhphung.todolist.entity.Task;

public class TaskMapper {
    public static TaskDto mapToTaskDto(Task task){
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDeadline(),
                task.isUrgent(),
                task.getCreatedAt(),
                task.isCompleted(),
                task.getDoneAt());
    }

    public static Task mapToTaskEntity(TaskDto taskDto){
        return new Task(
                taskDto.getId(),
                taskDto.getTitle(),
                taskDto.getDescription(),
                taskDto.getDeadline(),
                taskDto.isUrgent(),
                taskDto.getCreatedAt(),
                taskDto.isCompleted(),
                taskDto.getDoneAt());
    }
}
