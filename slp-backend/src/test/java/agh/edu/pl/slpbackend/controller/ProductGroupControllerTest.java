package agh.edu.pl.slpbackend.controller;

import agh.edu.pl.slpbackend.controller.dictionary.ProductGroupController;
import agh.edu.pl.slpbackend.dto.productGroup.ProductGroupDto;
import agh.edu.pl.slpbackend.service.dictionary.ProductGroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductGroupControllerTest {

    @Mock
    private ProductGroupService service;

    @Test
    void delete() {
        var controller = new ProductGroupController(service);
        Long id = 1L;
        var response = controller.delete(id);

        verify(service).delete(ProductGroupDto.builder().id(id).build());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
