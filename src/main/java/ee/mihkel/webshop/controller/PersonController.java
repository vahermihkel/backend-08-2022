package ee.mihkel.webshop.controller;

import ee.mihkel.webshop.entity.Person;
import ee.mihkel.webshop.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @GetMapping("persons")
    public ResponseEntity<List<Person>> getPersons() {
//        String personCode = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
//        Person person = personRepository.findById(personCode).get();
//        if (person.getFirstName().equals("Kati")) {
            return new ResponseEntity<>(personRepository.findAll(), HttpStatus.OK);
//        } else {
//            throw new Exception();
//        }
    }

    @PatchMapping("change-to-admin/{personCode}")
    public ResponseEntity<List<Person>> changePersonToAdmin(@PathVariable String personCode) {
        Person person = personRepository.findById(personCode).get();
        person.setRole("admin");
        personRepository.save(person);
        return new ResponseEntity<>(personRepository.findAll(), HttpStatus.OK);
    }

    @PatchMapping("change-to-user/{personCode}")
    public ResponseEntity<List<Person>> changePersonToUser(@PathVariable String personCode) {
        Person person = personRepository.findById(personCode).get();
        person.setRole(null);
        personRepository.save(person);
        return new ResponseEntity<>(personRepository.findAll(), HttpStatus.OK);
    }

    // ctrl + ä
    // code -> comment with line comment
//    @PostMapping("persons")
//    public List<Person> addPerson(@RequestBody Person person) {
//        if (!personRepository.existsById(person.getPersonCode())) {
//            personRepository.save(person);
//        }
//        return personRepository.findAll();
//    }
}
