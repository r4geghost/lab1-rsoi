package ru.dyusov.backend.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.dyusov.backend.domain.Person;
import ru.dyusov.backend.model.PersonRequest;
import ru.dyusov.backend.service.PersonService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.dyusov.backend.controller.ControllerTestUtils.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PersonControllerTest {
    private MockMvc mvc;

    @InjectMocks
    private PersonController controller;

    @Mock
    private PersonService personService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ErrorController())
                .build();
    }

    @Test
    void getPersonByIdSuccessWhenPersonExists() throws Exception {
        int id = 1;
        Person person = Person.build(
                id,
                "Mihail",
                23,
                "Moscow",
                "BMSTU"
        );
        when(personService.getPersonById(id)).thenReturn(person);
        mvc.perform(
                        get("/api/v1/persons/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Mihail"))
                .andExpect(jsonPath("$.age").value(23))
                .andExpect(jsonPath("$.address").value("Moscow"))
                .andExpect(jsonPath("$.work").value("BMSTU"));
    }

    @Test
    void getPersonByIdFailWhenPersonNotFound() throws Exception {
        int id = 1;
        when(personService.getPersonById(id)).thenThrow(new EntityNotFoundException("Person with id=" + id + " not found"));
        mvc.perform(get("/api/v1/persons/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Person with id=" + id + " not found"));
    }

    @Test
    void addPersonSuccessWhenRequestIsCorrect() throws Exception {
        PersonRequest request = PersonRequest.build(
                "Mihail",
                23,
                "Moscow",
                "BMSTU"
        );
        mvc.perform(postJson("/api/v1/persons/", request))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void addPersonFailWhenBodyIsMissing() throws Exception {
        PersonRequest request = new PersonRequest();
        mvc.perform(postJson("/api/v1/persons/", request))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPersonsSuccessWhenPersonsExist() throws Exception {
        List<Person> persons = List.of(
                Person.build(
                        1,
                        "Mihail",
                        23,
                        "Moscow",
                        "BMSTU"
                ), Person.build(
                        2,
                        "Andrey",
                        22,
                        "Moscow",
                        "MTUSI"
                ));
        when(personService.getPersons()).thenReturn(persons);
        mvc.perform(get("/api/v1/persons"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(persons.size()));

    }

    @Test
    void updatePersonWhenPersonExists() throws Exception {
        int id = 1;
        Person person = Person.build(
                id,
                "Mihail",
                23,
                "Moscow",
                "BMSTU"
        );
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "NewName");
        fieldsToUpdate.put("age", 18);
        Person updatedPerson = Person.build(
                person.getId(),
                (String) fieldsToUpdate.get("name"),
                (Integer) fieldsToUpdate.get("age"),
                person.getAddress(),
                person.getWork()
        );
        PersonRequest request = PersonRequest.build(
                updatedPerson.getName(),
                updatedPerson.getAge(),
                updatedPerson.getAddress(),
                updatedPerson.getWork()
        );
        when(personService.updatePerson(id, fieldsToUpdate)).thenReturn(updatedPerson);
        mvc.perform(patchJson("/api/v1/persons/{id}", id, fieldsToUpdate))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(fieldsToUpdate.get("name")))
                .andExpect(jsonPath("$.age").value(fieldsToUpdate.get("age")));
    }

    @Test
    void deletePersonSuccessWhenStatusNoContent() throws Exception {
        int id = 1;
        doNothing().when(personService).deletePerson(id);
        mvc.perform(deleteJson("/api/v1/persons/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}