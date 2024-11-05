package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.IndicationDto;
import agh.edu.pl.slpbackend.model.Indication;

public interface IndicationMapper {

    default IndicationDto toDto(final Indication model) {
        return IndicationDto.builder()
                .id(model.getId())
                .name(model.getName())
                .method(model.getMethod())
                .unit(model.getUnit())
                .laboratory(model.getLaboratory())
                .isOrganoleptic(model.isOrganoleptic())
                .build();
    }

    default Indication toModel(final IndicationDto dto) {
        return Indication.builder()
                .id(dto.getId())
                .name(dto.getName())
                .method(dto.getMethod())
                .unit(dto.getUnit())
                .laboratory(dto.getLaboratory())
                .isOrganoleptic(dto.isOrganoleptic())
                .build();
    }
}
