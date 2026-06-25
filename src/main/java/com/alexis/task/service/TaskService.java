package com.alexis.task.service;

import java.util.List;
import java.util.UUID;

import com.alexis.task.dto.CreateTaskRequest;
import com.alexis.task.dto.TaskResponse;

public interface TaskService {

    List<TaskResponse> findAll();

    List<TaskResponse> findByStatus(String status);

    TaskResponse findById(UUID id);

    TaskResponse createTask(CreateTaskRequest request);

    TaskResponse completeTask(UUID id);

    void deleteTask(UUID id);

}
