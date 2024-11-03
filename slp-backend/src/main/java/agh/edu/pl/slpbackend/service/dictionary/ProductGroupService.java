package agh.edu.pl.slpbackend.service.dictionary;

import agh.edu.pl.slpbackend.dto.productGroup.ProductGroupDto;
import agh.edu.pl.slpbackend.dto.productGroup.ProductGroupSaveDto;
import agh.edu.pl.slpbackend.mapper.ProductGroupMapper;
import agh.edu.pl.slpbackend.model.ProductGroup;
import agh.edu.pl.slpbackend.model.SamplingStandard;
import agh.edu.pl.slpbackend.repository.IndicationRepository;
import agh.edu.pl.slpbackend.repository.ProductGroupRepository;
import agh.edu.pl.slpbackend.repository.SamplingStandardRepository;
import agh.edu.pl.slpbackend.service.iface.AbstractService;
import agh.edu.pl.slpbackend.service.iface.IModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ProductGroupService extends AbstractService implements ProductGroupMapper {

    private final ProductGroupRepository productGroupRepository;
    private final IndicationRepository indicationRepository;
    private final SamplingStandardRepository samplingStandardRepository;

    public List<ProductGroupDto> selectAll() {
        List<ProductGroup> productGroupList = productGroupRepository.findAll();
        return productGroupList.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public Object insert(IModel model) {
        final ProductGroupSaveDto dto = (ProductGroupSaveDto) model;
        return productGroupRepository.save(toModel(createObjectToSave(dto)));
    }

    @Override
    public Object update(IModel model) {
        final ProductGroupSaveDto dto = (ProductGroupSaveDto) model;
        return productGroupRepository.save(toModel(createObjectToSave(dto)));
    }

    @Override
    public void delete(IModel model) {
        final ProductGroupDto dto = (ProductGroupDto) model;
        productGroupRepository.deleteById(dto.getId());
    }

    private ProductGroupDto createObjectToSave(ProductGroupSaveDto dto) {
        final ProductGroupDto dtoToSave = new ProductGroupDto();
        dtoToSave.setId(dto.getId());
        dtoToSave.setName(dto.getName());

        final List<SamplingStandard> samplingStandardList = new ArrayList<>();
        dto.getSamplingStandards().forEach(samplingStandardId -> createSampleStandardListToSave(samplingStandardId, samplingStandardList));

        dtoToSave.setSamplingStandards(samplingStandardList);

        return dtoToSave;
    }

    private void createSampleStandardListToSave(final Long id, final List<SamplingStandard> samplingStandardList) {
        final SamplingStandard samplingStandard = samplingStandardRepository.findById(id).orElseThrow();
        samplingStandardList.add(samplingStandard);
    }
}
