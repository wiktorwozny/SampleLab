package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.CodeDto;
import agh.edu.pl.slpbackend.exception.DataDependencyException;
import agh.edu.pl.slpbackend.repository.CodeRepository;
import agh.edu.pl.slpbackend.service.dictionary.CodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CodeServiceTest {

    @Mock
    private CodeRepository repository;

    @Test
    void delete() {
        var service = new CodeService(repository);
        String id = "Kp";

        service.delete(CodeDto.builder().id(id).build());

        verify(repository).deleteById(id);
    }

    @Test
    void delete_fails_when_data_integrity_violation_exception() {
        var service = new CodeService(repository);
        String id = "Kp";

        doThrow(DataIntegrityViolationException.class)
                .when(repository).deleteById(id);

        assertThatThrownBy(() -> service.delete(CodeDto.builder().id(id).build()))
                .isInstanceOf(DataDependencyException.class);
    }
}
