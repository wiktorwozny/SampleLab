package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.controller.DataController;
import agh.edu.pl.slpbackend.repository.ClientRepository;
import agh.edu.pl.slpbackend.repository.CodeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class DataTest {

    @Autowired
    private DataController controller;

    @Autowired
    private CodeRepository codeRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    void get_filters() {
        var response = controller.getFilters();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var filters = response.getBody();
        assertThat(filters).isNotNull();
        assertThat(filters.codes().size()).isEqualTo(codeRepository.count());
        assertThat(filters.clients().size()).isEqualTo(clientRepository.count());
        assertThat(filters.groups().size()).isEqualTo(2);
    }
}
