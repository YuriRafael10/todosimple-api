package com.yurirafael.todosimple.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.persistence.EntityNotFoundException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundExcpetion extends EntityNotFoundException {
    

    public ObjectNotFoundExcpetion(String message) {
        super(message);
    }

}
