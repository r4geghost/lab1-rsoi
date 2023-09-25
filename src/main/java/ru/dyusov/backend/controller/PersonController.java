package ru.dyusov.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.dyusov.backend.domain.Person;
import ru.dyusov.backend.model.PersonRequest;
import ru.dyusov.backend.service.PersonService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/persons")
    public ResponseEntity<Void> addPerson(@Valid @RequestBody PersonRequest person) {
        int id = personService.addPerson(person);
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri()).build();
    }

    @GetMapping("/persons/{id}")
    public Person getPersonById(@PathVariable("id") int id) {
        return personService.getPersonById(id);
    }

    @GetMapping("/persons")
    public List<Person> getPersons() {
        return personService.getPersons();
    }

    @PatchMapping("/persons/{id}")
    public Person updatePerson(@PathVariable int id, @RequestBody Map<String, Object> fields) {
        return personService.updatePerson(id, fields);
    }

    @DeleteMapping("/persons/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable("id") int id) {
        personService.deletePerson(id);
    }

}
