package com.mavha.codetest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mavha.codetest.domain.Person;

/**
 * This is a Spring Data JPA Person repository. Implements the communication with the db.
 * 
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

}
