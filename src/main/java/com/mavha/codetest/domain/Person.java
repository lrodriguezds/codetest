package com.mavha.codetest.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Person {

    @Id
    @NotNull(message = "Dni field should not be null")
    private Integer dni;

    @NotNull(message = "Firstname field should not be null")
    @Column(nullable = false)
    private String firstname;

    @NotNull(message = "Lastname field should not be null")
    @Column(nullable = false)
    private String lastname;

    @Column
    @NotNull(message = "Age field should not be null")
    @Min(value = 0, message = "Age field should be positive value")
    private Integer age;

    public Person() {
    }

    public Person(Integer dni, String firstname, String lastname, Integer age) {
        super();
        this.dni = dni;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
    }

    public Integer getDni() {
        return dni;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Integer getAge() {
        return age;
    }

}
