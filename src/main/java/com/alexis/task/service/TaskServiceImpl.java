package com.alexis.task.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.alexis.task.dto.CreateTaskRequest;
import com.alexis.task.dto.TaskResponse;
import com.alexis.task.exception.TaskNotFoundException;
import com.alexis.task.model.Task;
import com.alexis.task.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<TaskResponse> findAll() {
        return taskRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> findByStatus(String status) {
        if (!status.equalsIgnoreCase("completed") && !status.equalsIgnoreCase("pending")) {
            throw new IllegalArgumentException("Status must be 'completed' or 'pending' ");
        }

        boolean isCompleted = status.equalsIgnoreCase("completed");
        return taskRepository.findAll().stream()
                .filter(task -> task.isCompleted() == isCompleted)
                .map(this::toResponse)
                .toList();
    }

    @Override
    public TaskResponse findById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    return new TaskNotFoundException("Task not found with id: " + id);
                });

        return toResponse(task);
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = Task.builder()
                .id(UUID.randomUUID())
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        taskRepository.save(task);
        return toResponse(task);
    }

    @Override
    public TaskResponse completeTask(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        Task completed = Task.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(true)
                .createdAt(task.getCreatedAt())
                .build();

        return toResponse(taskRepository.save(completed));
    }

    @Override
    public void deleteTask(UUID id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id:" + id));
        
        taskRepository.deleteById(id);
    }

    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .createdAt(task.getCreatedAt())
                .build();
    }
}