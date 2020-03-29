package uk.co.huntersix.spring.rest.model;


import org.springframework.http.HttpStatus;

import java.util.Map;


public abstract class DomainException extends RuntimeException {
    public DomainException() {
    }

    public DomainException(String message) {
        super(message);
    }

    public DomainException(Throwable throwable) {
        super(throwable);
    }

    private HttpStatus httpStatus;
    private String errorCode;
    private Map<String, Object> params;


}
