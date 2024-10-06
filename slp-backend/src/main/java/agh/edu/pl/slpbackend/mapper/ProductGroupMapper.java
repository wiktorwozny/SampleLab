package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.ProductGroupDto;
import agh.edu.pl.slpbackend.model.ProductGroup;

public interface ProductGroupMapper {

    default ProductGroupDto toDto(final ProductGroup model) {
        return ProductGroupDto.builder()
                .id(model.getId())
                .name(model.getName())
                .indications(model.getIndications())
                .samplingStandards(model.getSamplingStandards())
                .build();
    }

    default ProductGroup toModel(final ProductGroupDto dto) {
        return ProductGroup.builder()
                .id(dto.getId())
                .name(dto.getName())
                .indications(dto.getIndications())
                .samplingStandards(dto.getSamplingStandards())
                .build();
    }
}
