package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.controller.SampleController;
import agh.edu.pl.slpbackend.dto.SampleDto;
import agh.edu.pl.slpbackend.dto.filters.FilterRequest;
import agh.edu.pl.slpbackend.dto.filters.Filters;
import agh.edu.pl.slpbackend.dto.filters.SummarySample;
import agh.edu.pl.slpbackend.enums.ProgressStatus;
import agh.edu.pl.slpbackend.mapper.SampleMapper;
import agh.edu.pl.slpbackend.repository.SampleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class SampleTest implements SampleMapper {

    @Autowired
    private SampleController controller;

    @Autowired
    private SampleRepository repository;

    @Test
    void get_all() {
        var response = controller.list();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var samples = response.getBody();
        assertThat(samples).isNotNull();
        assertThat(samples.size()).isEqualTo(repository.count());
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void add() {
        var count = repository.count();

        var sample = SampleDto.builder()
                .state("")
                .expirationComment("")
                .build();
        var response = controller.add(sample);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.count()).isEqualTo(count + 1);
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void update() {
        var sample = repository.findAll().get(0);
        var request = toDto(sample);
        String state = "test";
        request.setState(state);

        var response = controller.update(sample.getId(), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sample.getState()).isEqualTo(state);
    }

    @Test
    void count() {
        var response = controller.count();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(repository.count());
    }

    @Test
    void get_one() {
        var sample = repository.findAll().get(0);
        var response = controller.getOne(sample.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(toDto(sample));
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void update_status() {
        var sample = repository.findAll().get(0);
        var response = controller.updateStatus(sample.getId(), "IN_PROGRESS");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sample.getProgressStatus()).isEqualTo(ProgressStatus.IN_PROGRESS);

        response = controller.updateStatus(sample.getId(), "DONE");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sample.getProgressStatus()).isEqualTo(ProgressStatus.DONE);
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void delete() {
        var sample = repository.findAll().get(0);
        var response = controller.delete(sample.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.existsById(sample.getId())).isFalse();
    }

    @Test
    void filter() {
        var filters = new Filters(List.of("Kd", "O"), List.of("WIJHARS Kraków"), List.of(), List.of());
        var request = new FilterRequest("id", true, 1, 1, filters, "");
        var response = controller.filter(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var filterResult = response.getBody();
        assertThat(filterResult).isNotNull();
        assertThat(filterResult.totalPages()).isEqualTo(2);
        var sample = repository.findById(3L)
                .orElseThrow();
        assertThat(filterResult.samples()).isEqualTo(List.of(
                new SummarySample(sample.getId(), sample.getCode().getId(), sample.getAssortment().getGroup().getName(),
                        sample.getAssortment().getName(), sample.getClient().getName(), sample.getAdmissionDate(),
                        sample.getProgressStatus())
        ));
    }
}
