package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.assortment.AssortmentDto;
import agh.edu.pl.slpbackend.model.Assortment;

public interface AssortmentMapper {

    default Assortment toModel(final AssortmentDto dto) {
        return Assortment.builder()
                .id(dto.getId())
                .name(dto.getName())
                .group(dto.getGroup())
                .indications(dto.getIndications())
                .build();
    }

    default AssortmentDto toDto(final Assortment model) {
        return AssortmentDto.builder()
                .id(model.getId())
                .name(model.getName())
                .group(model.getGroup())
                .indications(model.getIndications())
                .build();
    }
}
