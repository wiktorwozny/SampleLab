package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.controller.dictionary.InspectionController;
import agh.edu.pl.slpbackend.dto.InspectionDto;
import agh.edu.pl.slpbackend.mapper.InspectionMapper;
import agh.edu.pl.slpbackend.repository.InspectionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class InspectionTest implements InspectionMapper {

    @Autowired
    private InspectionController controller;

    @Autowired
    private InspectionRepository repository;

    @Test
    void get_all() {
        var response = controller.list();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var inspections = response.getBody();
        assertThat(inspections).isNotNull();
        assertThat(inspections.size()).isEqualTo(repository.count());
    }

    @Test
    void add() {
        var count = repository.count();

        var inspection = InspectionDto.builder()
                .name("test")
                .build();
        var response = controller.add(inspection);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.count()).isEqualTo(count + 1);
    }

    @Test
    void update() {
        var inspection = repository.findAll().get(0);
        var request = toDto(inspection);
        String name = "test";
        request.setName(name);

        var response = controller.edit(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(inspection.getName()).isEqualTo(name);
    }
}
