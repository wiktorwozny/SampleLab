package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.controller.ReportDataController;
import agh.edu.pl.slpbackend.dto.ReportDataDto;
import agh.edu.pl.slpbackend.mapper.ReportDataMapper;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.repository.ReportDataRepository;
import agh.edu.pl.slpbackend.repository.SampleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class ReportDataTest implements ReportDataMapper {

    @Autowired
    private ReportDataController controller;

    @Autowired
    private ReportDataRepository repository;

    @Autowired
    private SampleRepository sampleRepository;

    @Test
    void get_all() {
        var response = controller.list();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var reportData = response.getBody();
        assertThat(reportData).isNotNull();
        assertThat(reportData.size()).isEqualTo(repository.count());
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void add() {
        var count = repository.count();

        Sample sample = sampleRepository.findAll().get(0);
        var request = ReportDataDto.builder()
                .sampleId(sample.getId())
                .build();
        var response = controller.add(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.count()).isEqualTo(count + 1);
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void update() {
        var reportData = repository.findAll().get(0);
        var request = toDto(reportData);
        String mechanism = "test";
        request.setMechanism(mechanism);

        var response = controller.update(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reportData.getMechanism()).isEqualTo(mechanism);
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void delete() {
        var reportData = repository.findAll().get(0);
        var response = controller.delete(reportData.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.existsById(reportData.getId())).isFalse();
    }

    @Test
    void get_by_sample_id() {
        var sample = sampleRepository.findAll().get(0);
        var response = controller.getReportBySampleId(sample.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var reportData = response.getBody();
        assertThat(reportData).isNotNull();
        assertThat(toModel(reportData)).isEqualTo(sample.getReportData());
    }
}
