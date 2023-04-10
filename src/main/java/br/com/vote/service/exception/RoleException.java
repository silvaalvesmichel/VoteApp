package br.com.vote.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class RoleException extends RuntimeException{
    public RoleException(String message) {
        super(message);
    }
}
