package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.ClientDto;
import agh.edu.pl.slpbackend.dto.CodeDto;
import agh.edu.pl.slpbackend.dto.SampleDto;
import agh.edu.pl.slpbackend.dto.filters.Filters;
import agh.edu.pl.slpbackend.model.Assortment;
import agh.edu.pl.slpbackend.model.ProductGroup;
import agh.edu.pl.slpbackend.service.dictionary.ClientService;
import agh.edu.pl.slpbackend.service.dictionary.CodeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DataService {
    private final CodeService codeService;
    private final ClientService clientService;
    private final SampleService sampleService;

    public Filters getFilters() {
        List<String> codes = codeService.selectAll().stream()
                .map(CodeDto::getId)
                .toList();
        List<String> clients = clientService.selectAll().stream()
                .map(ClientDto::getName)
                .toList();
        List<String> groups = sampleService.selectAll().stream()
                .map(SampleDto::getAssortment)
                .map(Assortment::getGroup)
                .map(ProductGroup::getName)
                .distinct()
                .toList();

        return new Filters(codes, clients, groups, null);
    }
}
