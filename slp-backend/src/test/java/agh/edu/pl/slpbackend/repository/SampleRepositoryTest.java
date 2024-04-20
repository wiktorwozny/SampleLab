package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.Sample;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Transactional
@SpringBootTest
public class SampleRepositoryTest {

    @Autowired
    private SampleRepository sampleRepository;


    private Sample getSaveExample() {
        //@formatter:off
        return Sample.builder()
                .id(1L)
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
    public void insert() {


    }

    @Test
    public void findAll() {
        final List<Sample> list = sampleRepository.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    public void save() {
        long count1 = this.sampleRepository.count();

        final Sample response = this.sampleRepository.save(getSaveExample());
        assertEquals(getSaveExample(), response);
        long count2 = this.sampleRepository.count();

        assertEquals(count1 + 1, count2);
    }
}
