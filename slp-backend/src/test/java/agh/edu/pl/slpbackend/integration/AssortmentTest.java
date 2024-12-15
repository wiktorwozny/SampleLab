package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.controller.dictionary.AssortmentController;
import agh.edu.pl.slpbackend.dto.AssortmentDto;
import agh.edu.pl.slpbackend.mapper.AssortmentMapper;
import agh.edu.pl.slpbackend.repository.AssortmentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class AssortmentTest implements AssortmentMapper {

    @Autowired
    private AssortmentController controller;

    @Autowired
    private AssortmentRepository repository;

    @Test
    void get_all() {
        var response = controller.list();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var assortments = response.getBody();
        assertThat(assortments).isNotNull();
        assertThat(assortments.size()).isEqualTo(repository.count());
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void add() {
        var count = repository.count();

        var assortment = AssortmentDto.builder()
                .name("test")
                .build();
        var response = controller.add(assortment);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.count()).isEqualTo(count + 1);
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void update() {
        var assortment = repository.findAll().get(0);
        var request = toDto(assortment);
        String name = "test";
        request.setName(name);

        var response = controller.edit(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(assortment.getName()).isEqualTo(name);
    }
}
