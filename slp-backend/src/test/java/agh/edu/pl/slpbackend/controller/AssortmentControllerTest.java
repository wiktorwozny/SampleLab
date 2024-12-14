package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.dictionary.AssortmentController;
import agh.edu.pl.slpbackend.dto.AssortmentDto;
import agh.edu.pl.slpbackend.service.dictionary.AssortmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AssortmentControllerTest {

    @Mock
    private AssortmentService service;

    @Test
    void delete() {
        var controller = new AssortmentController(service);
        Long id = 1L;
        var response = controller.delete(id);

        verify(service).delete(AssortmentDto.builder().id(id).build());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
