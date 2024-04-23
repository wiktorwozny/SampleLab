package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.AddressDto;
import agh.edu.pl.slpbackend.dto.ClientDto;
import agh.edu.pl.slpbackend.model.Address;
import agh.edu.pl.slpbackend.model.Client;

public interface AddressMapper {
    default AddressDto toDto(final Address model) {
        return AddressDto.builder()
                .id(model.getId())
                .street(model.getStreet())
                .zipCode(model.getZipCode())
                .city(model.getCity())
                .build();
    }

    default Address toModel(final AddressDto dto) {
        return Address.builder()
                .id(dto.getId())
                .zipCode(dto.getZipCode())
                .street(dto.getStreet())
                .city(dto.getCity())
                .build();
    }
}
