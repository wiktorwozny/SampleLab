package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.productGroup.ProductGroupDto;
import agh.edu.pl.slpbackend.model.ProductGroup;

public interface ProductGroupMapper {

    default ProductGroupDto toDto(final ProductGroup model) {
        return ProductGroupDto.builder()
                .id(model.getId())
                .name(model.getName())
                .samplingStandards(model.getSamplingStandards())
                .assortments(model.getAssortments())
                .build();
    }

    default ProductGroup toModel(final ProductGroupDto dto) {
        return ProductGroup.builder()
                .id(dto.getId())
                .name(dto.getName())
                .samplingStandards(dto.getSamplingStandards())
                .assortments(dto.getAssortments())
                .build();
    }
}
