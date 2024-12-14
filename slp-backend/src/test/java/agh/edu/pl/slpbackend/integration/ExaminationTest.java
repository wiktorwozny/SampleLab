package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.controller.ExaminationController;
import agh.edu.pl.slpbackend.dto.ExaminationDto;
import agh.edu.pl.slpbackend.mapper.ExaminationMapper;
import agh.edu.pl.slpbackend.repository.ExaminationRepository;
import agh.edu.pl.slpbackend.repository.SampleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class ExaminationTest implements ExaminationMapper {

    @Autowired
    private ExaminationController controller;

    @Autowired
    private ExaminationRepository repository;

    @Autowired
    private SampleRepository sampleRepository;


    @Test
    void add() {
        var count = repository.count();
        var sample = sampleRepository.findAll().get(0);
        var examination = ExaminationDto.builder()
                .sample(sample)
                .build();
        var response = controller.add(examination);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.count()).isEqualTo(count + 1);
    }

    @Test
    void update() {
        var examination = repository.findAll().get(0);
        var request = toDto(examination);
        String result = "test";
        request.setResult(result);

        var response = controller.insertExaminationResults(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(examination.getResult()).isEqualTo(result);
    }

    @Test
    void delete() {
        var examination = repository.findAll().get(0);
        var response = controller.deleteExamination(examination.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.existsById(examination.getId())).isFalse();
    }

    @Test
    void get_one() {
        var examination = repository.findAll().get(0);
        var response = controller.getExaminationById(examination.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(toDto(examination));
    }

    @Test
    void get_for_sample() {
        var sample = sampleRepository.findAll().get(0);

        var response = controller.getExaminationForSample(sample.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var examinations = response.getBody();
        assertThat(examinations).isNotNull();
        assertThat(examinations.size()).isEqualTo(sample.getAssortment().getIndications().size());
    }
}
