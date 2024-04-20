package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.ClientDto;
import agh.edu.pl.slpbackend.model.Client;

public interface ClientMapper {

    default ClientDto toDto(final Client model) {
        return ClientDto.builder()
                .id(model.getId())
                .name(model.getName())
                .wijharsCode(model.getWijharsCode())
                .address(model.getAddress())
                .build();
    }

    default Client toModel(final ClientDto dto) {
        return Client.builder()
                .id(dto.getId())
                .name(dto.getName())
                .wijharsCode(dto.getWijharsCode())
                .address(dto.getAddress())
                .build();
    }
}
