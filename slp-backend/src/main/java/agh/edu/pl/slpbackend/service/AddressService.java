package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.AddressDto;
import agh.edu.pl.slpbackend.mapper.AddressMapper;
import agh.edu.pl.slpbackend.model.Address;
import agh.edu.pl.slpbackend.repository.AddressRepository;
import agh.edu.pl.slpbackend.service.iface.AbstractService;
import agh.edu.pl.slpbackend.service.iface.IModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AddressService extends AbstractService implements AddressMapper {

    private final AddressRepository addressRepository;

    public List<AddressDto> selectAll() {
        List<Address> addressesList = addressRepository.findAll();
        return addressesList.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public Object insert(IModel model) {
        final AddressDto addressDto = (AddressDto) model;
        final Address address = toModel(addressDto);
        return addressRepository.save(address);
    }

    @Override
    public Object update(IModel model) {
        return null;
    }


    @Override
    public void delete(IModel model) {
    }
}
