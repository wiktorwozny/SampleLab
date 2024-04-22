package agh.edu.pl.slpbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Sample not found")
public class SampleNotFoundException extends RuntimeException {
}
