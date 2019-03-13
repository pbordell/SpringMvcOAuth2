package com.pbs.springmvc.service;

import java.util.List;

import com.pbs.springmvc.model.Person;

public interface PersonService {

	Person findById(Long id);

	Person findByName(String name);

	void saveOrUpdatePerson(Person user);

	void deletePersonById(Long id);

	List<Person> findAllPersons();

	public boolean isPersonExist(Person person);

}
