package br.com.vote.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AlreadyInUseException extends RuntimeException {
    public AlreadyInUseException(String message) {
        super(message);
    }
}
