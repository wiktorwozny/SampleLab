package agh.edu.pl.slpbackend.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Wrong password")
public class WrongPasswordException extends RuntimeException {
}
