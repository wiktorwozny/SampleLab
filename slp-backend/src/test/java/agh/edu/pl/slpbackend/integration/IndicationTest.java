package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.controller.dictionary.IndicationController;
import agh.edu.pl.slpbackend.dto.IndicationDto;
import agh.edu.pl.slpbackend.exception.SampleNotFoundException;
import agh.edu.pl.slpbackend.mapper.IndicationMapper;
import agh.edu.pl.slpbackend.repository.IndicationRepository;
import agh.edu.pl.slpbackend.repository.SampleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
public class IndicationTest implements IndicationMapper {

    @Autowired
    private IndicationController controller;

    @Autowired
    private IndicationRepository repository;

    @Autowired
    private SampleRepository sampleRepository;

    @Test
    void get_all() {
        var response = controller.list();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var indications = response.getBody();
        assertThat(indications).isNotNull();
        assertThat(indications.size()).isEqualTo(repository.count());
    }

    @Test
    void add() {
        var count = repository.count();

        var indication = IndicationDto.builder()
                .name("test")
                .build();
        var response = controller.add(indication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.count()).isEqualTo(count + 1);
    }

    @Test
    void update() {
        var indication = repository.findAll().get(0);
        var request = toDto(indication);
        String name = "test";
        request.setName(name);

        var response = controller.edit(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(indication.getName()).isEqualTo(name);
    }

    @Test
    void get_one() {
        var indication = repository.findAll().get(0);

        var response = controller.getIndicationById(indication.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(toDto(indication));
    }

    @Test
    void get_for_sample() {
        var sample = sampleRepository.findAll().get(0);

        var response = controller.getIndicationsForSample(sample.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var indications = response.getBody();
        assertThat(indications).isNotNull();
        assertThat(indications.size()).isEqualTo(sample.getAssortment().getIndications().size());
    }

    @Test
    void get_for_sample_fails_when_sample_not_found() {
        assertThatThrownBy(() -> controller.getIndicationsForSample(8634685L))
                .isInstanceOf(SampleNotFoundException.class);
    }
}
