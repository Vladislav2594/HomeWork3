package ru.digitalhabits.homework3.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.digitalhabits.homework3.model.DepartmentFullResponse;
import ru.digitalhabits.homework3.model.DepartmentRequest;
import ru.digitalhabits.homework3.service.DepartmentService;
import ru.digitalhabits.homework3.service.PersonService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DepartmentController.class)
class DepartmentControllerTest {

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private PersonService personService;

    @Autowired
    private MockMvc mockMvc;

    private DepartmentRequest departmentRequest;

    @BeforeEach
    void setUp() {
        departmentRequest = new DepartmentRequest();
        departmentRequest.setName("IT Department");
    }

    @Test
    void departments() throws Exception {
        mockMvc.perform(get("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void department() throws Exception {
        when(departmentService.getById(1)).thenReturn(new DepartmentFullResponse());

        mockMvc.perform(get("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void createDepartment() throws Exception {
        when(departmentService.create(any(DepartmentRequest.class))).thenReturn(1);

        mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"IT Department\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/departments/1"));
    }

    @Test
    void updateDepartment() throws Exception {
        when(departmentService.update(any(Integer.class), any(DepartmentRequest.class)))
                .thenReturn(new DepartmentFullResponse());

        mockMvc.perform(patch("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Department\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Department"));
    }

    @Test
    void deleteDepartment() throws Exception {
        doNothing().when(departmentService).delete(1);

        mockMvc.perform(delete("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void addPersonToDepartment() throws Exception {
        doNothing().when(personService).addPersonToDepartment(1, 1);

        mockMvc.perform(post("/api/v1/departments/1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void removePersonFromDepartment() throws Exception {
        doNothing().when(personService).removePersonFromDepartment(1, 1);

        mockMvc.perform(delete("/api/v1/departments/1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void closeDepartment() throws Exception {
        doNothing().when(departmentService).close(1);

        mockMvc.perform(post("/api/v1/departments/1/close")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}