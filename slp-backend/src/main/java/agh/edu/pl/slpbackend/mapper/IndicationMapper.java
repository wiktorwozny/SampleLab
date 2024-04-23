package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.IndicationDto;
import agh.edu.pl.slpbackend.model.Indication;

public interface IndicationMapper {

    default IndicationDto toDto(final Indication model) {
        return IndicationDto.builder()
                .id(model.getId())
                .name(model.getName())
                .norm(model.getNorm())
                .unit(model.getUnit())
                .laboratory(model.getLaboratory())
                .build();
    }

    default Indication toModel(final IndicationDto dto) {
        return Indication.builder()
                .id(dto.getId())
                .name(dto.getName())
                .norm(dto.getNorm())
                .unit(dto.getUnit())
                .laboratory(dto.getLaboratory())
                .build();
    }
}
