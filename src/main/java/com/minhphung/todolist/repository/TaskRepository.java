package com.minhphung.todolist.repository;

import com.minhphung.todolist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByCompletedEquals(boolean completed);
    List<Task> findByUrgentEquals(boolean urgent);
}
