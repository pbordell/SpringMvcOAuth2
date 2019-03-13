package com.pbs.springmvc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pbs.springmvc.model.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long>{

	Person findByName(String name);

}
