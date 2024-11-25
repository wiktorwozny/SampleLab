package agh.edu.pl.slpbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Cannot delete record because other data depend on it")
public class DataDependencyException extends RuntimeException {
}
