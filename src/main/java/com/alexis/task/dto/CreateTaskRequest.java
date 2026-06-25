package com.alexis.task.dto;

import lombok.Data;

@Data
public class CreateTaskRequest {
    
    private String title;
    private String description;
}
