package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.controller.dictionary.SamplingStandardController;
import agh.edu.pl.slpbackend.dto.SamplingStandardDto;
import agh.edu.pl.slpbackend.mapper.SamplingStandardMapper;
import agh.edu.pl.slpbackend.repository.SamplingStandardRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class SamplingStandardTest implements SamplingStandardMapper {

    @Autowired
    private SamplingStandardController controller;

    @Autowired
    private SamplingStandardRepository repository;

    @Test
    void get_all() {
        var response = controller.list();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var samplingStandards = response.getBody();
        assertThat(samplingStandards).isNotNull();
        assertThat(samplingStandards.size()).isEqualTo(repository.count());
    }

    @Test
    void add() {
        var count = repository.count();

        var samplingStandard = SamplingStandardDto.builder()
                .name("test")
                .build();
        var response = controller.add(samplingStandard);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.count()).isEqualTo(count + 1);
    }

    @Test
    void update() {
        var samplingStandard = repository.findAll().get(0);
        var request = toDto(samplingStandard);
        String name = "test";
        request.setName(name);

        var response = controller.edit(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(samplingStandard.getName()).isEqualTo(name);
    }
}
