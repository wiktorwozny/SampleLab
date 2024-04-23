package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.AddressDto;
import agh.edu.pl.slpbackend.dto.SampleDto;
import agh.edu.pl.slpbackend.mapper.AddressMapper;
import agh.edu.pl.slpbackend.mapper.ClientMapper;
import agh.edu.pl.slpbackend.model.Address;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.repository.AddressRepository;
import agh.edu.pl.slpbackend.service.iface.AbstractService;
import agh.edu.pl.slpbackend.service.iface.IModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AddressService extends AbstractService implements AddressMapper {
    private final AddressRepository addressRepository;

    @Override
    public ResponseEntity<?> insert(IModel model) {
        final AddressDto addressDto = (AddressDto) model;
        final Address address = toModel(addressDto);
        final Address saveResult = addressRepository.save(address);

        return new ResponseEntity<>(saveResult, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> update(IModel model) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(IModel model) {
        return null;
    }

    public List<AddressDto> selectAll() {
        List<Address> addressesList = addressRepository.findAll();
        return addressesList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
