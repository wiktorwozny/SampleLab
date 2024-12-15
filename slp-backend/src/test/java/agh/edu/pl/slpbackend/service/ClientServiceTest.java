package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.ClientDto;
import agh.edu.pl.slpbackend.exception.DataDependencyException;
import agh.edu.pl.slpbackend.repository.ClientRepository;
import agh.edu.pl.slpbackend.service.dictionary.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @Test
    void delete() {
        var service = new ClientService(repository);
        Long id = 1L;

        service.delete(ClientDto.builder().id(id).build());

        verify(repository).deleteById(id);
    }

    @Test
    void delete_fails_when_data_integrity_violation_exception() {
        var service = new ClientService(repository);
        Long id = 1L;

        doThrow(DataIntegrityViolationException.class)
                .when(repository).deleteById(id);

        assertThatThrownBy(() -> service.delete(ClientDto.builder().id(id).build()))
                .isInstanceOf(DataDependencyException.class);
    }
}
