package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.SamplingStandardDto;
import agh.edu.pl.slpbackend.model.SamplingStandard;

public interface SamplingStandardMapper {

    default SamplingStandardDto toDto(final SamplingStandard model) {
        return SamplingStandardDto.builder()
                .id(model.getId())
                .name(model.getName())
                .groups(model.getGroups())
                .build();
    }


    default SamplingStandard toModel(final SamplingStandardDto dto) {
        return SamplingStandard.builder()
                .id(dto.getId())
                .name(dto.getName())
                .groups(dto.getGroups())
                .build();
    }
}
