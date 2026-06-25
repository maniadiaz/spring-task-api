package com.alexis.task.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
}
