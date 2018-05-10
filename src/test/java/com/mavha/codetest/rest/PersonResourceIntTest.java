package com.mavha.codetest.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mavha.codetest.domain.Person;
import com.mavha.codetest.exception.ExceptionHandlerController;
import com.mavha.codetest.repository.PersonRepository;

/**
 * Test class for the PersonResource REST controller.
 *
 * @see PersonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@ActiveProfiles("test")
public class PersonResourceIntTest {

    private static final Integer DNI = 31734207;
    private static final String FIRSTNAME = "Leandro";
    private static final String LASTNAME = "Rodriguez";
    private static final int AGE = 32;

    @Autowired
    private PersonResource personResource;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionHandlerController exceptionHandlerController;

    @Autowired
    private EntityManager em;

    private MockMvc restPersonMockMvc;

    private Person person;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        this.restPersonMockMvc = MockMvcBuilders.standaloneSetup(personResource)
                .setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionHandlerController)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create a person for the test.
     */
    public static Person createEntity(EntityManager em) {
        Person person = new Person(DNI, FIRSTNAME, LASTNAME, AGE);
        return person;
    }

    @Before
    public void initTest() {
        person = createEntity(em);
    }

    @Test
    @Transactional
    public void getPeople() throws Exception {
        personRepository.saveAndFlush(person);

        restPersonMockMvc.perform(get("/personas")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].dni").value(hasItem(DNI)))
                .andExpect(jsonPath("$.[*].firstname").value(hasItem(FIRSTNAME)))
                .andExpect(jsonPath("$.[*].lastname").value(hasItem(LASTNAME)))
                .andExpect(jsonPath("$.[*].age").value(hasItem(AGE)));

        personRepository.delete(person);
    }

    @Test
    @Transactional
    public void createPerson() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // Create the Person
        restPersonMockMvc.perform(post("/personas").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(convertObjectToJsonBytes(person))).andExpect(status().isCreated());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate + 1);
        Person testPerson = personList.get(personList.size() - 1);

        assertThat(testPerson.getDni()).isEqualTo(DNI);
        assertThat(testPerson.getFirstname()).isEqualTo(FIRSTNAME);
        assertThat(testPerson.getLastname()).isEqualTo(LASTNAME);
        assertThat(testPerson.getAge()).isEqualTo(AGE);
    }

    @Test
    @Transactional
    public void createPersonWithExistingDniMustFail() throws Exception {
        // Create the Person with an existing DNI in DB
        person = new Person(DNI, FIRSTNAME, LASTNAME, AGE);
        personRepository.save(person);

        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // A person with an existing DNI cannot be created, so this API call must fail
        restPersonMockMvc.perform(post("/personas").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(convertObjectToJsonBytes(person))).andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();

        // set firstname field with null
        person = new Person(DNI, null, LASTNAME, AGE);

        // Create the Person, which fails
        restPersonMockMvc.perform(post("/personas").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(convertObjectToJsonBytes(person))).andExpect(status().isBadRequest());

        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();

        // set lastname field with null
        person = new Person(DNI, FIRSTNAME, null, AGE);

        // Create the Person, which fails
        restPersonMockMvc.perform(post("/personas").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(convertObjectToJsonBytes(person))).andExpect(status().isBadRequest());

        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();

        // set age field with null
        person = new Person(DNI, FIRSTNAME, LASTNAME, null);

        // Create the Person, which fails
        restPersonMockMvc.perform(post("/personas").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(convertObjectToJsonBytes(person))).andExpect(status().isBadRequest());

        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAgeIsGreatherThanZero() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();

        // set age field with negative value
        person = new Person(DNI, FIRSTNAME, LASTNAME, -1);

        // Create the Person which fails
        restPersonMockMvc.perform(post("/personas").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(convertObjectToJsonBytes(person))).andExpect(status().isBadRequest());

        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    /**
     * Convert an object to JSON byte array.
     */
    private byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);

        return mapper.writeValueAsBytes(object);
    }
}
