package ru.digitalhabits.homework3.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.digitalhabits.homework3.model.*;
import ru.digitalhabits.homework3.service.PersonService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    void persons() throws Exception {
        when(personService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/persons")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());

        verify(personService, times(1)).findAll();
    }

    @Test
    void person() throws Exception {
        PersonFullResponse person = new PersonFullResponse(1, "John", 27, null);
        when(personService.getById(1)).thenReturn(person);

        mockMvc.perform(get("/api/v1/persons/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        verify(personService, times(1)).getById(1);
    }

    @Test
    void createPerson() throws Exception {
        PersonRequest personRequest = new PersonRequest("John", "Doe", "Jdawd", 25);
        when(personService.create(any(PersonRequest.class))).thenReturn(1);

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(personService, times(1)).create(any(PersonRequest.class));
    }

    @Test
    void updatePerson() throws Exception {
        PersonFullResponse updatedPerson = new PersonFullResponse(1, "John", 25, null);
        when(personService.update(eq(1), any(PersonRequest.class))).thenReturn(updatedPerson);

        mockMvc.perform(patch("/api/v1/persons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        verify(personService, times(1)).update(eq(1), any(PersonRequest.class));
    }

    @Test
    void deletePerson() throws Exception {
        doNothing().when(personService).delete(1);

        mockMvc.perform(delete("/api/v1/persons/1"))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(personService, times(1)).delete(1);
    }
}
