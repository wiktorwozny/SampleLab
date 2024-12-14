package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.dictionary.InspectionController;
import agh.edu.pl.slpbackend.dto.InspectionDto;
import agh.edu.pl.slpbackend.service.dictionary.InspectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class InspectionControllerTest {

    @Mock
    private InspectionService service;

    @Test
    void delete() {
        var controller = new InspectionController(service);
        Long id = 1L;
        var response = controller.delete(id);

        verify(service).delete(InspectionDto.builder().id(id).build());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
