package com.mavha.codetest.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mavha.codetest.domain.Person;
import com.mavha.codetest.service.PersonService;

/**
 * 
 * REST controller implements people fetching and person saving logic.
 *
 */
@RestController
@RequestMapping("/personas")
public class PersonResource {

    @Autowired
    private PersonService personService;

    @GetMapping
    public List<Person> getPeople() {
        return personService.getPeople();
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person person) throws URISyntaxException {

        Person result = personService.save(person);

        return ResponseEntity.created(new URI("/personas/" + result.getDni())).body(result);

    }
}
