package uk.co.huntersix.spring.rest.model;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Person already exists")
public class PersonAlreadyExistException extends DomainException {
    public PersonAlreadyExistException() {
        super("Person already exists");
    }
}
