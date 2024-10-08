package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.ClientDto;
import agh.edu.pl.slpbackend.dto.CodeDto;
import agh.edu.pl.slpbackend.dto.ProductGroupDto;
import agh.edu.pl.slpbackend.dto.filters.Filters;
import agh.edu.pl.slpbackend.service.dictionary.ClientService;
import agh.edu.pl.slpbackend.service.dictionary.CodeService;
import agh.edu.pl.slpbackend.service.dictionary.ProductGroupService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DataService {
    private final CodeService codeService;
    private final ClientService clientService;
    private final ProductGroupService groupService;

    public Filters getFilters() {
        List<String> codes = codeService.selectAll().stream()
                .map(CodeDto::getId)
                .toList();
        List<String> clients = clientService.selectAll().stream()
                .map(ClientDto::getName)
                .toList();
        List<String> groups = groupService.selectAll().stream()
                .map((ProductGroupDto::getName))
                .toList();

        return new Filters(codes, clients, groups, null);
    }
}
