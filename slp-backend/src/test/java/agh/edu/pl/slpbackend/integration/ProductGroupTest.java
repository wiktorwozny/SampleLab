package agh.edu.pl.slpbackend.integration;

import agh.edu.pl.slpbackend.controller.dictionary.ProductGroupController;
import agh.edu.pl.slpbackend.dto.productGroup.ProductGroupSaveDto;
import agh.edu.pl.slpbackend.mapper.ProductGroupMapper;
import agh.edu.pl.slpbackend.repository.ProductGroupRepository;
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
public class ProductGroupTest implements ProductGroupMapper {

    @Autowired
    private ProductGroupController controller;

    @Autowired
    private ProductGroupRepository repository;

    @Test
    void get_all() {
        var response = controller.list();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var groups = response.getBody();
        assertThat(groups).isNotNull();
        assertThat(groups.size()).isEqualTo(repository.count());
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void add() {
        var count = repository.count();

        var group = ProductGroupSaveDto.builder()
                .samplingStandards(List.of())
                .name("test")
                .build();
        var response = controller.add(group);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.count()).isEqualTo(count + 1);
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void update() {
        var group = repository.findAll().get(0);
        String name = "test";
        var request = ProductGroupSaveDto.builder()
                .id(group.getId())
                .name(name)
                .samplingStandards(List.of())
                .build();

        var response = controller.edit(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(group.getName()).isEqualTo(name);
    }
}
