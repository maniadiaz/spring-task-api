package com.alexis.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getTask_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createTask_shouldReturn201() throws Exception {
        String body = """
                {
                    "title": "Tarea de integración",
                    "description": "Descripción"
                }
                """;

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Tarea de integración"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void getTaskById_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(get("/api/tasks/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void completeTask_shouldReturn200() throws Exception {
        String body = """
                {
                    "title": "Tarea a completar",
                    "description": "Desc"
                }
                """;

        String response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn().getResponse().getContentAsString();

        String id = new ObjectMapper()
                .readTree(response).get("id").asString();

        mockMvc.perform(put("/api/tasks/" + id + "/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void deleteTask_shouldReturn204() throws Exception {
        String body = """
                {
                    "title": "Tarea a eliminar",
                    "description": "Desc"
                }
                """;

        String response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn().getResponse().getContentAsString();

        String id = new ObjectMapper()
                .readTree(response).get("id").asString();

        mockMvc.perform(delete("/api/tasks/" + id))
                .andExpect(status().isNoContent());
    }
}
