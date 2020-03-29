package uk.co.huntersix.spring.rest.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnPersonDetails()  {
        assertThat(
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/person/smith/mary",
                        String.class
                )
        ).contains("Mary");
    }

    @Test
    public void shouldReturnNotFoundWhenPersonNotFound() {
        Map entity = this.restTemplate.getForObject("http://localhost:" + port + "/person/kmd/trn", Map.class);
        assertNotNull(entity);
        assertEquals(404, entity.get("status"));
        assertEquals("Person not found", entity.get("message"));
    }

    @Test
    public void shouldReturnMultipleResultWhileSearchBySurname()  {
        Person[] personList = this.restTemplate.getForObject("http://localhost:" + port + "/person/smith", Person[].class);
        assertNotNull(personList);
        assertThat(personList.length > 0);
        assertFalse(Arrays.stream(personList).filter(t -> !t.getLastName().equalsIgnoreCase("smith")).findAny().isPresent());
    }

    @Test
    public void shouldReturnEmptyArrayForNoPeople() {
        Person[] personList = this.restTemplate.getForObject("http://localhost:" + port + "/person/kmd", Person[].class);
        assertNotNull(personList);
        assertThat(personList.length == 0);
    }


    @Test
    public void shouldCreateNewPerson()  {
        Map<String, String> map = new HashMap<>();
        map.put("firstName", "Marvel");
        map.put("lastName", "Bryn");
        HttpEntity<Map> request = new HttpEntity<>(map);
        ResponseEntity<Person> response = restTemplate
                .exchange("http://localhost:" + port + "/person", HttpMethod.POST, request, Person.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals("Marvel", response.getBody().getFirstName());
        assertEquals("Bryn", response.getBody().getLastName());
    }

    @Test
    public void shouldReturnBadRequestWhenPersonAlreadyExist()  {
        Map<String, String> map = new HashMap<>();
        map.put("firstName", "Mary");
        map.put("lastName", "Smith");
        HttpEntity<Map> request = new HttpEntity<>(map);
        ResponseEntity<Map> response = restTemplate
                .exchange("http://localhost:" + port + "/person", HttpMethod.POST, request, Map.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Person already exists", response.getBody().get("message"));
    }


    @Test
    public void shouldUpdateFirstNameOfPerson() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "1");
        map.put("firstName", "Hilary");
        HttpEntity<Map> request = new HttpEntity<>(map);
        ResponseEntity<Person> response = restTemplate
                .exchange("http://localhost:" + port + "/person", HttpMethod.PUT, request, Person.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hilary", response.getBody().getFirstName());
        assertEquals(new Long(1), response.getBody().getId());
    }

    @Test
    public void shouldReturnNotFoundIfPersonNotExist() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "-11");
        map.put("firstName", "Hilary");
        HttpEntity<Map> request = new HttpEntity<>(map);
        ResponseEntity<Map> response = restTemplate
                .exchange("http://localhost:" + port + "/person", HttpMethod.PUT, request, Map.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Person not found", response.getBody().get("message"));
    }

}