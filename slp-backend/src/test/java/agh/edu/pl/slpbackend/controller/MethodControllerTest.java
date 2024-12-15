package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.service.MethodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MethodControllerTest {

    @Mock
    private MethodService service;

    @Mock
    private MultipartFile file;

    @Mock
    private InputStream inputStream;

    @BeforeEach
    void setUp() throws IOException {
        when(file.getInputStream()).thenReturn(inputStream);
    }

    @Test
    void import_methods() throws IOException {
        var controller = new MethodController(service);
        var response = controller.importMethods(file);

        verify(service).importMethods(inputStream);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void import_methods_fails_when_exception() throws IOException {
        var controller = new MethodController(service);
        doThrow(IOException.class).when(service).importMethods(inputStream);
        var response = controller.importMethods(file);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
