package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.dictionary.CodeController;
import agh.edu.pl.slpbackend.dto.CodeDto;
import agh.edu.pl.slpbackend.service.dictionary.CodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CodeControllerTest {

    @Mock
    private CodeService service;

    @Test
    void delete() {
        var controller = new CodeController(service);
        String id = "Kp";
        var response = controller.delete(id);

        verify(service).delete(CodeDto.builder().id(id).build());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
