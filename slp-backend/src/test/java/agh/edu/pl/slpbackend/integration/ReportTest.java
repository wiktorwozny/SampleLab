package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.exception.SampleNotFoundException;
import agh.edu.pl.slpbackend.reports.kzwa.KZWAReportGeneratorController;
import agh.edu.pl.slpbackend.reports.samplereport.SampleReportGeneratorController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ReportTest {

    @Autowired
    private KZWAReportGeneratorController KZWAReportController;

    @Autowired
    private SampleReportGeneratorController sampleReportController;

    @Test
    void generate_KZWA_report() {
        var response = KZWAReportController.generate(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void generate_KZWA_report_fails_when_unknown_sample_id() {
        assertThatThrownBy(() -> KZWAReportController.generate(34895L))
                .isInstanceOf(SampleNotFoundException.class);
    }

    @Test
    void generate_sample_report_fails_when_unknown_sample_id() {
        assertThatThrownBy(() -> sampleReportController.generate(34895L, ""))
                .isInstanceOf(SampleNotFoundException.class);
    }

    @Test
    void generate_F4_report0() {
        var response = sampleReportController.generate(1L, "F4");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void generate_F5_report0() {
        var response = sampleReportController.generate(1L, "F5");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void generate_F4_report1() {
        var response = sampleReportController.generate(4L, "F4");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void generate_F4_report2() {
        var response = sampleReportController.generate(5L, "F4");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void generate_F4_report3() {
        var response = sampleReportController.generate(6L, "F4");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void generate_F4_report4() {
        var response = sampleReportController.generate(7L, "F4");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void generate_F5_report1() {
        var response = sampleReportController.generate(4L, "F5");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void generate_F5_report2() {
        var response = sampleReportController.generate(5L, "F5");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void generate_F5_report3() {
        var response = sampleReportController.generate(6L, "F5");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void generate_F5_report4() {
        var response = sampleReportController.generate(7L, "F5");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
