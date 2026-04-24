package ee.piperal.veebipood.controller;

import ee.piperal.veebipood.dto.PersonLoginRecordDto;
import ee.piperal.veebipood.entity.Person;
import ee.piperal.veebipood.repository.PersonRepository;
import ee.piperal.veebipood.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    //Dependency injection, if this class (PersonController) is created, is connected at the same time
    @Autowired
    private PersonService personService;

    @GetMapping("person")
    public List<Person> getPerson() {
        return personRepository.findAll();
    }

    @GetMapping("person/{id}")
    public Person getPersonById(@PathVariable Long id) {
        return personRepository.findById(id).orElseThrow();
    }

    @DeleteMapping("person/{id}")
    public List<Person> delPerson(@PathVariable Long id) {
        personRepository.deleteById(id);
        return personRepository.findAll();
    }


    @PostMapping("signup")
    public Person updateProfile(@RequestBody Person person) {
        if (person.getId() != null) {
            throw new RuntimeException("cannot sign up with id");
        }
        personService.validate(person);
        return personRepository.save(person);
    }

    @PostMapping("login")
    public Person login(@RequestBody PersonLoginRecordDto personDto) {
        Person dbperson = personRepository.findByEmail(personDto.email);
        if (dbperson == null) {
            throw new RuntimeException("Invalid email");
        }
        if (!dbperson.getPassword().equals(personDto.password)) {
            throw new RuntimeException("Invalid password");
        }
        return dbperson;
    }

    @PutMapping("profile")
    public Person signup(@RequestBody Person person) { // TODO: PersonsignupDTO (without address)
        if (person.getId() == null) {
            throw new RuntimeException("id not provided, cannot update profile");
        }
        personService.validate(person);
        return personRepository.save(person);
    }

    @GetMapping("profile")
    public Person getProfile(@RequestParam Long id) {
        return personRepository.findById(id).orElseThrow();
    }

}
