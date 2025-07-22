package com.minhphung.todolist.service.impl;

import com.minhphung.todolist.dto.TaskDto;
import com.minhphung.todolist.entity.Task;
import com.minhphung.todolist.exception.ResourceNotFoundException;
import com.minhphung.todolist.mapper.TaskMapper;
import com.minhphung.todolist.repository.TaskRepository;
import com.minhphung.todolist.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task task = TaskMapper.mapToTaskEntity(taskDto);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.mapToTaskDto(savedTask);
    }

    @Override
    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(TaskMapper::mapToTaskDto).collect(Collectors.toList());
    }

    @Override
    public TaskDto getTaskById(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        return TaskMapper.mapToTaskDto(task);
    }

    @Override
    public TaskDto updateTask(UUID taskId, TaskDto taskDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setUrgent(taskDto.isUrgent());
        task.setDeadline(taskDto.getDeadline());

        //if complete is set from false to true, set DoneAt as the current time
        if (!task.isCompleted() && taskDto.isCompleted()) {
            task.setDoneAt(LocalDateTime.now());
        }
        //if complete is set from true back to false, set DoneAt to null
        else if (task.isCompleted() && !taskDto.isCompleted()) {
            task.setDoneAt(null);
        }

        task.setCompleted(taskDto.isCompleted());

        Task savedTask = taskRepository.save(task);
        return TaskMapper.mapToTaskDto(savedTask);
    }

    @Override
    public TaskDto markAsCompleted(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

            task.setCompleted(true);
            task.setDoneAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);
        return TaskMapper.mapToTaskDto(savedTask);
    }

    @Override
    public void deleteTask(UUID taskId) {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        taskRepository.deleteById(taskId);
    }

    @Override
    public List<TaskDto> getAllCompletedTasks() {
        List<Task> tasks = taskRepository.findByCompletedEquals(true);
        return tasks.stream().map(TaskMapper::mapToTaskDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getAllIncompleteTasks() {
        List<Task> tasks = taskRepository.findByCompletedEquals(false);
        return tasks.stream().map(TaskMapper::mapToTaskDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getAllUrgentTasks() {
        List<Task> tasks = taskRepository.findByUrgentEquals(true);
        return tasks.stream().map(TaskMapper::mapToTaskDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getAllNonUrgentTasks() {
        List<Task> tasks = taskRepository.findByUrgentEquals(false);
        return tasks.stream().map(TaskMapper::mapToTaskDto).collect(Collectors.toList());
    }
}
