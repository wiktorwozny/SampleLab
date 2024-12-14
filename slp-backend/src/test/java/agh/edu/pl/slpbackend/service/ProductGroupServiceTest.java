package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.productGroup.ProductGroupDto;
import agh.edu.pl.slpbackend.exception.DataDependencyException;
import agh.edu.pl.slpbackend.repository.ProductGroupRepository;
import agh.edu.pl.slpbackend.repository.SamplingStandardRepository;
import agh.edu.pl.slpbackend.service.dictionary.ProductGroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductGroupServiceTest {

    @Mock
    private ProductGroupRepository repository;

    @Mock
    private SamplingStandardRepository samplingStandardRepository;

    @Test
    void delete() {
        var service = new ProductGroupService(repository, samplingStandardRepository);
        Long id = 1L;

        service.delete(ProductGroupDto.builder().id(id).build());

        verify(repository).deleteById(id);
    }

    @Test
    void delete_fails_when_data_integrity_violation_exception() {
        var service = new ProductGroupService(repository, samplingStandardRepository);
        Long id = 1L;

        doThrow(DataIntegrityViolationException.class)
                .when(repository).deleteById(id);

        assertThatThrownBy(() -> service.delete(ProductGroupDto.builder().id(id).build()))
                .isInstanceOf(DataDependencyException.class);
    }
}
