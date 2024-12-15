package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.dictionary.ClientController;
import agh.edu.pl.slpbackend.dto.ClientDto;
import agh.edu.pl.slpbackend.service.dictionary.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @Mock
    private ClientService service;

    @Test
    void delete() {
        var controller = new ClientController(service);
        Long id = 1L;
        var response = controller.delete(id);

        verify(service).delete(ClientDto.builder().id(id).build());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
