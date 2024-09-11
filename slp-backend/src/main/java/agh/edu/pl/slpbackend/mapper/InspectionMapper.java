package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.InspectionDto;
import agh.edu.pl.slpbackend.model.Inspection;

public interface InspectionMapper {

    default InspectionDto toDto(final Inspection model) {
        return InspectionDto.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

    default Inspection toModel(final InspectionDto dto) {
        return Inspection.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
