package com.minhphung.todolist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class TaskOverlappingException extends RuntimeException{
    public TaskOverlappingException(String message) {
        super(message);
    }
}
