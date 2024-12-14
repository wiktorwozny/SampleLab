package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.AssortmentDto;
import agh.edu.pl.slpbackend.exception.DataDependencyException;
import agh.edu.pl.slpbackend.repository.AssortmentRepository;
import agh.edu.pl.slpbackend.service.dictionary.AssortmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AssortmentServiceTest {

    @Mock
    private AssortmentRepository repository;

    @Test
    void delete() {
        var service = new AssortmentService(repository);
        Long id = 1L;

        service.delete(AssortmentDto.builder().id(id).build());

        verify(repository).deleteById(id);
    }

    @Test
    void delete_fails_when_data_integrity_violation_exception() {
        var service = new AssortmentService(repository);
        Long id = 1L;

        doThrow(DataIntegrityViolationException.class)
                .when(repository).deleteById(id);

        assertThatThrownBy(() -> service.delete(AssortmentDto.builder().id(id).build()))
                .isInstanceOf(DataDependencyException.class);
    }
}
