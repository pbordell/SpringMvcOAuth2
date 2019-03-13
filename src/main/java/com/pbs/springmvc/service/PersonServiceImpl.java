package com.pbs.springmvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pbs.springmvc.model.Person;
import com.pbs.springmvc.repository.PersonRepository;

@Service("personService")
public class PersonServiceImpl implements PersonService {

	@Autowired(required=true)
	private PersonRepository personRepository;

	public List<Person> findAllPersons() {
		return (List<Person>) personRepository.findAll();
	}

	public Person findById(Long id) {
		return personRepository.findById(id).get(); 
	}

	public Person findByName(String name) {
		return personRepository.findByName(name); 
	}

	public void saveOrUpdatePerson(Person Person) {
		personRepository.save(Person);
	}

	public void deletePersonById(Long id) {
		personRepository.deleteById(id);
	}

	public boolean isPersonExist(Person person) {
		return findByName(person.getName()) != null;
	}

}
