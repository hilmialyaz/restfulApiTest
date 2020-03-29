package uk.co.huntersix.spring.rest.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.huntersix.spring.rest.Application;
import uk.co.huntersix.spring.rest.model.PersonAlreadyExistException;
import uk.co.huntersix.spring.rest.model.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class PersonDataServiceTest {
    @Autowired
    private PersonDataService personDataService;

    @Test(expected = PersonNotFoundException.class)
    public void shouldThrowExWhenPersonNotFound() {
        personDataService.findPerson("test", "test2");
    }

    @Test
    public void shouldReturnSinglePersonForMatch() {
        List<Person> result = personDataService.findPerson("Smith");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Smith", result.get(0).getLastName());
    }

    @Test
    public void shouldReturnListForMultipleMatch() {
        List<Person> result = personDataService.findPerson("Brown");
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void shouldAddPerson(){
        Person person = new Person("Tom", "Sow");
        Person personSaved = personDataService.addPerson(person);
        assertNotNull(personSaved);
        assertEquals(person,personSaved);

        Person personFound = personDataService.findPerson(person.getLastName(), person.getFirstName());
        assertNotNull(personFound);
        assertEquals(person,personFound);

    }

    @Test(expected= PersonAlreadyExistException.class)
    public void shouldFailForExistingEntity() {
        Person person = new Person("Tom", "Sow");
        personDataService.addPerson(person);
        personDataService.addPerson(person);
    }


    @Test
    public void shouldUpdatePersonFirstName(){
        Person person = new Person("Tom", "Sow");
        Person personSaved = personDataService.addPerson(person);

        Person personUpdated = personDataService.updatePerson(personSaved.getId(), "SowV2");
        assertNotNull(personUpdated);
        assertEquals(personSaved.getId(),personUpdated.getId());
        assertEquals("SowV2",personUpdated.getFirstName());

        Person personFound = personDataService.findPerson(personUpdated.getLastName(), personUpdated.getFirstName());
        assertNotNull(personFound);
        assertEquals(personUpdated,personFound);
    }

    @Test(expected = PersonNotFoundException.class)
    public void shouldThrowPersonNotFoundWhileUpdatingFirstName() {
        personDataService.updatePerson(-1l, "test2");
    }
}
