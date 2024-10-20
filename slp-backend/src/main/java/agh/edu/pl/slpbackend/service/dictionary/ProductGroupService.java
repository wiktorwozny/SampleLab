package agh.edu.pl.slpbackend.service.dictionary;

import agh.edu.pl.slpbackend.dto.ProductGroupDto;
import agh.edu.pl.slpbackend.mapper.ProductGroupMapper;
import agh.edu.pl.slpbackend.model.ProductGroup;
import agh.edu.pl.slpbackend.repository.ProductGroupRepository;
import agh.edu.pl.slpbackend.service.iface.AbstractService;
import agh.edu.pl.slpbackend.service.iface.IModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ProductGroupService extends AbstractService implements ProductGroupMapper {

    private final ProductGroupRepository productGroupRepository;

    public List<ProductGroupDto> selectAll() {
        List<ProductGroup> productGroupList = productGroupRepository.findAll();
        return productGroupList.stream().map(this::toDto).toList();
    }

    @Override
    public Object insert(IModel model) {
        final ProductGroupDto dto = (ProductGroupDto) model;
        return productGroupRepository.save(toModel(dto));
    }

    @Override
    public Object update(IModel model) {
        final ProductGroupDto dto = (ProductGroupDto) model;
        return productGroupRepository.save(toModel(dto));
    }

    @Override
    public void delete(IModel model) {
        final ProductGroupDto dto = (ProductGroupDto) model;
        productGroupRepository.deleteById(dto.getId());
    }
}
