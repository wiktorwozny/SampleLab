package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.SamplingStandardDto;
import agh.edu.pl.slpbackend.exception.DataDependencyException;
import agh.edu.pl.slpbackend.model.ProductGroup;
import agh.edu.pl.slpbackend.model.SamplingStandard;
import agh.edu.pl.slpbackend.repository.ProductGroupRepository;
import agh.edu.pl.slpbackend.repository.SamplingStandardRepository;
import agh.edu.pl.slpbackend.service.dictionary.SamplingStandardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SamplingStandardServiceTest {

    @Mock
    private SamplingStandardRepository repository;

    @Mock
    private ProductGroupRepository groupRepository;

    @Test
    void delete() {
        var service = new SamplingStandardService(repository, groupRepository);
        Long id = 1L;

        var group = new ProductGroup(id, "name", List.of(), List.of());
        var samplingStandard = new SamplingStandard(id, "name", new ArrayList<>(List.of(group)));
        group.setSamplingStandards(new ArrayList<>(List.of(samplingStandard)));

        when(repository.findById(id))
                .thenReturn(Optional.of(samplingStandard));

        service.delete(SamplingStandardDto.builder().id(id).build());

        verify(groupRepository).save(group);
        verify(repository).save(samplingStandard);
        verify(repository).deleteById(id);
    }

    @Test
    void delete_fails_when_data_integrity_violation_exception() {
        var service = new SamplingStandardService(repository, groupRepository);
        Long id = 1L;

        var samplingStandard = new SamplingStandard(id, "name", new ArrayList<>());

        when(repository.findById(id))
                .thenReturn(Optional.of(samplingStandard));

        doThrow(DataIntegrityViolationException.class)
                .when(repository).deleteById(id);

        assertThatThrownBy(() -> service.delete(SamplingStandardDto.builder().id(id).build()))
                .isInstanceOf(DataDependencyException.class);
    }
}
