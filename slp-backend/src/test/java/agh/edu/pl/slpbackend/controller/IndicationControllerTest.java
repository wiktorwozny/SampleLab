package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.dictionary.IndicationController;
import agh.edu.pl.slpbackend.dto.IndicationDto;
import agh.edu.pl.slpbackend.service.dictionary.IndicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class IndicationControllerTest {

    @Mock
    private IndicationService service;

    @Test
    void delete() {
        var controller = new IndicationController(service);
        Long id = 1L;
        var response = controller.delete(id);

        verify(service).delete(IndicationDto.builder().id(id).build());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
