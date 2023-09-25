package ru.dyusov.backend.service;

import ru.dyusov.backend.domain.Person;
import org.springframework.stereotype.Service;
import ru.dyusov.backend.model.PersonRequest;

import java.util.List;
import java.util.Map;

@Service
public interface PersonService {
    public int addPerson(PersonRequest request);
    public Person getPersonById(int id);
    public List<Person> getPersons();
    public void deletePerson(int id);
    public Person updatePerson(int id, Map<String, Object> fields);}
