package agh.edu.pl.slpbackend.service.dictionary;

import agh.edu.pl.slpbackend.dto.ClientDto;
import agh.edu.pl.slpbackend.exception.DataDependencyException;
import agh.edu.pl.slpbackend.mapper.ClientMapper;
import agh.edu.pl.slpbackend.model.Client;
import agh.edu.pl.slpbackend.repository.ClientRepository;
import agh.edu.pl.slpbackend.service.iface.AbstractService;
import agh.edu.pl.slpbackend.service.iface.IModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ClientService extends AbstractService implements ClientMapper {

    private final ClientRepository clientRepository;

    public List<ClientDto> selectAll() {
        log.info("select all client");
        List<Client> clientList = clientRepository.findAll();
        return clientList.stream().map(this::toDto).collect(Collectors.toList());
    }


    @Override
    public Object insert(IModel model) {
        log.info("insert client");
        final ClientDto dto = (ClientDto) model;
        return clientRepository.save(toModel(dto));
    }

    @Override
    public Object update(IModel model) {
        log.info("update client");
        final ClientDto dto = (ClientDto) model;
        return clientRepository.save(toModel(dto));
    }

    @Override
    public void delete(IModel model) {
        log.info("delete client");
        final ClientDto dto = (ClientDto) model;
        try {
            clientRepository.deleteById(dto.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DataDependencyException();
        }
    }
}
