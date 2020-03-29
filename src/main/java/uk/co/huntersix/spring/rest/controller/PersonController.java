package uk.co.huntersix.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.List;

@RestController
public class PersonController {
    private PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public Person person(@PathVariable(value = "lastName") String lastName,
                         @PathVariable(value = "firstName") String firstName) {
        return personDataService.findPerson(lastName, firstName);
    }

    @GetMapping("/person/{lastName}")
    public List<Person> person(@PathVariable(value = "lastName") String lastName) {
        return personDataService.findPerson(lastName);
    }


    @PostMapping("/person")
    public Person add(@RequestBody Person p) {
        return personDataService.addPerson(p);
    }

    @PutMapping("/person")
    public Person update( @RequestBody Person p) {
        return personDataService.updatePerson(p.getId(),p.getFirstName());
    }



}