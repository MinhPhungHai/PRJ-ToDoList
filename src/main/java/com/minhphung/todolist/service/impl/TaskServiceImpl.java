package com.minhphung.todolist.service.impl;

import com.minhphung.todolist.dto.TaskDto;
import com.minhphung.todolist.entity.Task;
import com.minhphung.todolist.exception.InvalidTimeRangeException;
import com.minhphung.todolist.exception.ResourceNotFoundException;
import com.minhphung.todolist.exception.TaskOverlappingException;
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
    public boolean validateTaskTimeRangeOverlap(TaskDto taskDto) {
        List<Task> tasks = taskRepository.findAll();
        boolean overlapCheck = false;
        for (Task existingTask : tasks){
            if (!(existingTask.getId() == taskDto.getId())){//ignore when loop through itself

                //2 tasks A B are overlap when A.starttime() is before B.endtime() or A.endtime() is before B.starttime()
                overlapCheck = taskDto.getStartTime().isBefore(existingTask.getEndTime())
                        && taskDto.getEndTime().isAfter(existingTask.getStartTime());
            }
        }
        return overlapCheck;
    }

    @Override
    public boolean hasValidTimeRange(TaskDto taskDto) {
        if (taskDto.getStartTime().isAfter(taskDto.getEndTime())) return false;
        return true;
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        //check if time range is valid
        if (!hasValidTimeRange(taskDto)){
            throw new InvalidTimeRangeException("Invalid time range (Start time must be before End time)!");
        }

        //check if timeline overlap any other task
        if (validateTaskTimeRangeOverlap(taskDto)) {
            throw new TaskOverlappingException("Task timeline overlaps another task!");
        }

        Task task = TaskMapper.mapToTaskEntity(taskDto);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.mapToTaskDto(savedTask);
    }

    @Override
    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        //sorting: urgent tasks -> near deadline tasks
        tasks.sort((task1, task2) -> {
            // 1. Prioritize urgent tasks
            if (task1.isUrgent() && !task2.isUrgent()) return -1;
            else if (!task1.isUrgent() && task2.isUrgent()) return 1;

            else {// 2. If urgency is the same, compare deadlines
                if (task1.getDeadline().isBefore(task2.getDeadline())) return -1;
                else if (task1.getDeadline().isAfter(task2.getDeadline())) return 1;
                else return 0;
            }
        });

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

        //check if time range is valid
        if (!hasValidTimeRange(taskDto)){
            throw new InvalidTimeRangeException("Invalid time range (Start time must be before End time)!");
        }

        //check if timeline overlap any other task
        if (validateTaskTimeRangeOverlap(taskDto)) {
            throw new TaskOverlappingException("Task timeline overlaps another task!");
        }

        // mapstruct
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
