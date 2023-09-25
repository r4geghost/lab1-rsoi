package ru.dyusov.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.dyusov.backend.domain.Person;
import ru.dyusov.backend.model.PersonRequest;
import ru.dyusov.backend.repository.PersonRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Override
    public int addPerson(PersonRequest request) {
        Person saved = Person.build(
                0,
                request.getName(),
                request.getAge(),
                request.getAddress(),
                request.getWork()
        );
        personRepository.save(saved);
        return saved.getId();
    }

    @Override
    public Person getPersonById(int id) throws EntityNotFoundException {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            return person.get();
        } else {
            throw new EntityNotFoundException("Person with id=" + id + " not found");
        }
    }

    @Override
    public List<Person> getPersons() {
        return (List<Person>) personRepository.findAll();
    }

    @Override
    public void deletePerson(int id) {
        personRepository.deleteById(id);
    }

    @Override
    public Person updatePerson(int id, Map<String, Object> fields) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Person.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, person.get(), value);
            });
            return personRepository.save(person.get());
        }
        return null;
    }

}
