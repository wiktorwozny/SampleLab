package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.controller.dictionary.CodeController;
import agh.edu.pl.slpbackend.dto.CodeDto;
import agh.edu.pl.slpbackend.mapper.CodeMapper;
import agh.edu.pl.slpbackend.repository.CodeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class CodeTest implements CodeMapper {

    @Autowired
    private CodeController controller;

    @Autowired
    private CodeRepository repository;

    @Test
    void get_all() {
        var response = controller.list();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var codes = response.getBody();
        assertThat(codes).isNotNull();
        assertThat(codes.size()).isEqualTo(repository.count());
    }

    @Test
    void add() {
        var code = CodeDto.builder()
                .id("test")
                .build();
        var response = controller.add(code);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.existsById(code.getId())).isTrue();
    }

    @Test
    void update() {
        var code = repository.findAll().get(0);
        var request = toDto(code);
        String name = "test";
        request.setName(name);

        var response = controller.edit(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(code.getName()).isEqualTo(name);
    }
}
