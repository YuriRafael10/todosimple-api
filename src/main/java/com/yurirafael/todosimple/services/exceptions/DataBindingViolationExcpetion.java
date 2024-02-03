package com.yurirafael.todosimple.services.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DataBindingViolationExcpetion extends DataIntegrityViolationException{
    
    public DataBindingViolationExcpetion(String message)  {
        super(message);
    }
}
