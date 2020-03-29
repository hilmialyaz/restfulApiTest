package uk.co.huntersix.spring.rest.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.huntersix.spring.rest.Application;
import uk.co.huntersix.spring.rest.model.PersonNotFoundException;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class PersonDataServiceTest {
    @Autowired
    private PersonDataService personDataService;

    @Test(expected = PersonNotFoundException.class)
    public void shouldThrowExWhenPersonNotFound(){
        personDataService.findPerson("test","test2");
    }
}
