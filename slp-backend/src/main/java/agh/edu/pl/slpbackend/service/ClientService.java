package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.ClientDto;
import agh.edu.pl.slpbackend.mapper.ClientMapper;
import agh.edu.pl.slpbackend.model.Client;
import agh.edu.pl.slpbackend.repository.ClientRepository;
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
public class ClientService extends AbstractService implements ClientMapper {

    private final ClientRepository clientRepository;

    public List<ClientDto> selectAll() {
        List<Client> clientList = clientRepository.findAll();
        return clientList.stream().map(this::toDto).collect(Collectors.toList());
    }


    @Override
    public Object insert(IModel model) {
        return null;
    }

    @Override
    public Object update(IModel model) {
        return null;
    }

    @Override
    public void delete(IModel model) {
    }
}
