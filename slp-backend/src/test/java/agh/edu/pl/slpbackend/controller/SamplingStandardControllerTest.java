package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.dictionary.SamplingStandardController;
import agh.edu.pl.slpbackend.dto.SamplingStandardDto;
import agh.edu.pl.slpbackend.service.dictionary.SamplingStandardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SamplingStandardControllerTest {

    @Mock
    private SamplingStandardService service;

    @Test
    void delete() {
        var controller = new SamplingStandardController(service);
        Long id = 1L;
        var response = controller.delete(id);

        verify(service).delete(SamplingStandardDto.builder().id(id).build());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
