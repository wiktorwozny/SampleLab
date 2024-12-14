package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.IndicationDto;
import agh.edu.pl.slpbackend.exception.DataDependencyException;
import agh.edu.pl.slpbackend.repository.IndicationRepository;
import agh.edu.pl.slpbackend.repository.SampleRepository;
import agh.edu.pl.slpbackend.service.dictionary.IndicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class IndicationServiceTest {

    @Mock
    private IndicationRepository repository;

    @Mock
    private SampleRepository sampleRepository;

    @Test
    void delete() {
        var service = new IndicationService(repository, sampleRepository);
        Long id = 1L;

        service.delete(IndicationDto.builder().id(id).build());

        verify(repository).deleteById(id);
    }

    @Test
    void delete_fails_when_data_integrity_violation_exception() {
        var service = new IndicationService(repository, sampleRepository);
        Long id = 1L;

        doThrow(DataIntegrityViolationException.class)
                .when(repository).deleteById(id);

        assertThatThrownBy(() -> service.delete(IndicationDto.builder().id(id).build()))
                .isInstanceOf(DataDependencyException.class);
    }
}
