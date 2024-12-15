package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.InspectionDto;
import agh.edu.pl.slpbackend.exception.DataDependencyException;
import agh.edu.pl.slpbackend.repository.InspectionRepository;
import agh.edu.pl.slpbackend.service.dictionary.InspectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class InspectionServiceTest {

    @Mock
    private InspectionRepository repository;

    @Test
    void delete() {
        var service = new InspectionService(repository);
        Long id = 1L;

        service.delete(InspectionDto.builder().id(id).build());

        verify(repository).deleteById(id);
    }

    @Test
    void delete_fails_when_data_integrity_violation_exception() {
        var service = new InspectionService(repository);
        Long id = 1L;

        doThrow(DataIntegrityViolationException.class)
                .when(repository).deleteById(id);

        assertThatThrownBy(() -> service.delete(InspectionDto.builder().id(id).build()))
                .isInstanceOf(DataDependencyException.class);
    }
}
