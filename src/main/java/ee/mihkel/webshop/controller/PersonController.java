package ee.mihkel.webshop.controller;

import ee.mihkel.webshop.entity.Person;
import ee.mihkel.webshop.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @GetMapping("persons")
    public List<Person> getPersons() {
        return personRepository.findAll();
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
