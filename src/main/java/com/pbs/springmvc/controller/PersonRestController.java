package com.pbs.springmvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.pbs.springmvc.model.Person;
import com.pbs.springmvc.service.PersonService;

@RestController
@PreAuthorize("isAuthenticated()")
public class PersonRestController {

	@Autowired
	private PersonService personService;
	

	// -------------------Retrieve All Persons--------------------------------------------------------

	@GetMapping(value = "/person/")
	@PreAuthorize("hasPermission(null)")
	public ResponseEntity<List<Person>> listAllPersons() {
		List<Person> persons = personService.findAllPersons();
		if (persons.isEmpty()) {
			return new ResponseEntity<List<Person>>(HttpStatus.NO_CONTENT); 
		}
		
		return new ResponseEntity<List<Person>>(persons, HttpStatus.OK);
	}

	// -------------------Retrieve Single Person--------------------------------------------------------

	@GetMapping(value = "/person/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@PreAuthorize("hasPermission(null)")
	public ResponseEntity<Person> getPerson(@PathVariable("id") long id) {
		Person person = personService.findById(id);
		if (person == null) {
			return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Person>(person, HttpStatus.OK);
	}

	// -------------------Create a Person--------------------------------------------------------

	@PostMapping(value = "/person/")
	public ResponseEntity<Void> createPerson(@RequestBody Person person, UriComponentsBuilder ucBuilder) {

		if (personService.isPersonExist(person)) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

		personService.saveOrUpdatePerson(person);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/person/{id}").buildAndExpand(person.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a Person --------------------------------------------------------

	@PutMapping(value = "/person/{id}")
	public ResponseEntity<Person> updatePerson(@PathVariable("id") long id, @RequestBody Person person) {
		Person currentPerson = personService.findById(id);

		if (currentPerson == null) {
			return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
		}

		currentPerson.setName(person.getName());
		currentPerson.setAge(person.getAge());
		currentPerson.setSalary(person.getSalary());

		personService.saveOrUpdatePerson(currentPerson);
		return new ResponseEntity<Person>(currentPerson, HttpStatus.OK);
	}

	// ------------------- Delete a Person --------------------------------------------------------

	@DeleteMapping(value = "/person/{id}")
	public ResponseEntity<Person> deletePerson(@PathVariable("id") long id) {
		Person person = personService.findById(id);
		if (person == null) {
			return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
		}

		personService.deletePersonById(id);
		return new ResponseEntity<Person>(HttpStatus.NO_CONTENT);
	}

}