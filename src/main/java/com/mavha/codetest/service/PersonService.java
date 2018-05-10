package com.mavha.codetest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mavha.codetest.domain.Person;
import com.mavha.codetest.exception.PersonAlreadyExistsException;
import com.mavha.codetest.exception.PersonNotFoundException;
import com.mavha.codetest.repository.PersonRepository;

/**
 * Person service in charge of implements the creation, get list of people and get a
 * particular person.
 *
 */
@Service
@Transactional
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public List<Person> getPeople() {

        return personRepository.findAll();
    }

    public Person save(Person person) {

        boolean personExists = personRepository.existsById(person.getDni());

        if (personExists) {
            throw new PersonAlreadyExistsException(person.getDni(),
                    "Person with dni: " + person.getDni() + " already exists");
        }

        return personRepository.save(person);
    }

    public Person getUser(Integer dni) {
        Optional<Person> person = personRepository.findById(dni);

        if (!person.isPresent()) {
            throw new PersonNotFoundException(dni, "Person not found");
        }

        return person.get();
    }

}