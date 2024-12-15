package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.controller.AddressController;
import agh.edu.pl.slpbackend.repository.AddressRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class AddressTest {

    @Autowired
    private AddressController controller;

    @Autowired
    private AddressRepository repository;

    @Test
    void get_all() {
        var response = controller.list();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var addresses = response.getBody();
        assertThat(addresses).isNotNull();
        assertThat(addresses.size()).isEqualTo(repository.count());
    }
}
