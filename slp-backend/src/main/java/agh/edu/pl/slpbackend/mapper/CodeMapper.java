package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.CodeDto;
import agh.edu.pl.slpbackend.model.Code;

public interface CodeMapper {

    default CodeDto toDto(final Code model) {
        return CodeDto.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

    default Code toModel(final CodeDto dto) {
        return Code.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
