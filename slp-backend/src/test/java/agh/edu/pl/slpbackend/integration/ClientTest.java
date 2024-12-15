package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.controller.dictionary.ClientController;
import agh.edu.pl.slpbackend.dto.ClientDto;
import agh.edu.pl.slpbackend.mapper.ClientMapper;
import agh.edu.pl.slpbackend.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class ClientTest implements ClientMapper {

    @Autowired
    private ClientController controller;

    @Autowired
    private ClientRepository repository;

    @Test
    void get_all() {
        var response = controller.list();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var clients = response.getBody();
        assertThat(clients).isNotNull();
        assertThat(clients.size()).isEqualTo(repository.count());
    }

    @Test
    void add() {
        var count = repository.count();

        var client = ClientDto.builder()
                .name("test")
                .build();
        var response = controller.add(client);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.count()).isEqualTo(count + 1);
    }

    @Test
    void update() {
        var client = repository.findAll().get(0);
        var request = toDto(client);
        String name = "test";
        request.setName(name);

        var response = controller.edit(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(client.getName()).isEqualTo(name);
    }
}