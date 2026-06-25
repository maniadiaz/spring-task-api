package com.alexis.task.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.alexis.task.model.Task;

@Repository
public class InMemoryTaskRepository implements TaskRepository {
    private final HashMap<UUID, Task> task = new HashMap<>();

    @Override
    public Task save(Task task) {
        Objects.requireNonNull(task, "Task must not be null");

        this.task.put(task.getId(), task);

        return task;
    }

    @Override
    public Optional<Task> findById(UUID id) {
        Objects.requireNonNull(id, "UUID must not be null");

        return Optional.ofNullable(task.get(id));

    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(task.values());
    }

    @Override
    public void deleteById(UUID id) {
        Objects.requireNonNull(id, "UUID must not be null");
        task.remove(id);
    }

}
