package agh.edu.pl.slpbackend.service;

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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class SampleServiceTest {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private SampleRepository sampleRepository;

    private SampleDto getSaveExample() {
        //@formatter:off
        return SampleDto.builder()
                .id(1L)
                .code(null)
                .client(null)
                .assortment("test")
                .admissionDate(LocalDate.now())
                .expirationDateComment("test")
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
    public void insert() {

        long count1 = this.sampleRepository.count();

        final ResponseEntity<Sample> response = this.sampleService.insert(getSaveExample());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        long count2 = this.sampleRepository.count();

        assertEquals(count1 + 1, count2);
    }
}
