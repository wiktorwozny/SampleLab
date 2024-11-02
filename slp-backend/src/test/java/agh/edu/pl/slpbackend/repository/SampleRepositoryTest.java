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
                .code(null)
                .client(null)
                .assortment(null)
                .admissionDate(LocalDate.now())
                .expirationComment("test")
                .examinationExpectedEndDate(LocalDate.now())
                .size("test")
                .state("test")
                .analysis(Boolean.TRUE)
                .inspection(null)
                .samplingStandard(null)
                .reportData(null)
                .build();
        //@formatter:on
    }


    @Test
    public void findAll() {
        final List<Sample> list = sampleRepository.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    public void save() {

        final long count1 = this.sampleRepository.count();

        final Sample response = this.sampleRepository.save(getSaveExample());

        final long count2 = this.sampleRepository.count();
        assertEquals(count1 + 1, count2);

    }

}
