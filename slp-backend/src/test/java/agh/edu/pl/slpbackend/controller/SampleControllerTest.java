package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.dto.SampleDto;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.repository.SampleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Transactional
@SpringBootTest
public class SampleControllerTest {

    @Autowired
    private SampleController sampleController;

    @Autowired
    private SampleRepository sampleRepository;

    private SampleDto getSaveExample() {
        //@formatter:off
        return SampleDto.builder()
                .code(null)
                .client(null)
                .assortment("test")
                .admissionDate(LocalDate.now())
                .expirationComment("test")
                .examinationEndDate(LocalDate.now())
                .size("test")
                .state("test")
                .analysis(Boolean.TRUE)
                .inspection(null)
                .group(null)
                .samplingStandard(null)
                .reportData(null)
                .build();
        //@formatter:on
    }

    @Test
    public void add() {
        final ResponseEntity<Sample> response = this.sampleController.add(getSaveExample());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void list() {
        final ResponseEntity<List<SampleDto>> list = this.sampleController.list();
        assertEquals(HttpStatus.OK, list.getStatusCode());
        assertFalse(Objects.requireNonNull(list.getBody()).isEmpty());
    }
}
