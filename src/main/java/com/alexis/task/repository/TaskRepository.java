package com.alexis.task.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.alexis.task.model.Task;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(UUID id);

    List<Task> findAll();
    
    void deleteById(UUID id);
}