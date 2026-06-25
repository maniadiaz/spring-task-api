package com.alexis.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alexis.task.dto.CreateTaskRequest;
import com.alexis.task.dto.TaskResponse;
import com.alexis.task.exception.TaskNotFoundException;
import com.alexis.task.model.Task;
import com.alexis.task.repository.TaskRepository;
import com.alexis.task.service.TaskServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void findAll_shouldReturnEmptyList_whenNoTaskExist() {

        // Arrange
        when(taskRepository.findAll()).thenReturn(List.of());

        // Act
        List<TaskResponse> result = taskService.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldResturnTaskList_whenTasksExist() {
        // Arrange
        Task task = Task.builder()
                .id(UUID.randomUUID())
                .title("Test task")
                .description("Description")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();
        when(taskRepository.findAll()).thenReturn(List.of(task));

        // Act
        List<TaskResponse> result = taskService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test task", result.get(0).getTitle());
    }

    @Test
    void findById_shouldReturnTaskResponse_whenTaskExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        Task task = Task.builder()
                .id(id)
                .title("Test task")
                .description("Description")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        // Act
        TaskResponse result = taskService.findById(id);

        // Assert
        assertEquals(id, result.getId());
        assertEquals("Test task", result.getTitle());
    }

    @Test
    void findById_shouldThrowException_whenTaskNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.findById(id));
    }

    @Test
    void createTask_shouldReturnTaskReponse_whenTaskIsCreated() {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Nueva tarea");
        request.setDescription("Description");

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskResponse result = taskService.createTask(request);

        // Assert
        assertEquals("Nueva tarea", result.getTitle());
        assertFalse(result.isCompleted());
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void completeTask_shouldReturnCompletedTask_whenTaskExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        Task task = Task.builder()
                .id(id)
                .title("Tarea")
                .description("Desc")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskResponse result = taskService.completeTask(id);

        // Assert
        assertTrue(result.isCompleted());
    }

    @Test
    void completeTask_shouldThrowException_whenTaskNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.completeTask(id));
    }

    @Test
    void deleteTask_shouldCallDeleteById_whenTaskExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        Task task = Task.builder()
                .id(id)
                .title("Tarea")
                .description("Desc")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        // Act
        taskService.deleteTask(id);

        // Assert
        verify(taskRepository).deleteById(id);
    }

    @Test
    void deleteTask_shouldThrowException_whenTaskNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(id));
    }

    @Test
    void findByStatus_shouldThrowException_whenStatusIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> taskService.findByStatus("invalido"));
    }
}
