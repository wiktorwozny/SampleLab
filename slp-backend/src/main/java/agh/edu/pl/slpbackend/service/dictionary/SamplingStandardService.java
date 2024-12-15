package agh.edu.pl.slpbackend.service.dictionary;

import agh.edu.pl.slpbackend.dto.SamplingStandardDto;
import agh.edu.pl.slpbackend.exception.DataDependencyException;
import agh.edu.pl.slpbackend.mapper.SamplingStandardMapper;
import agh.edu.pl.slpbackend.model.ProductGroup;
import agh.edu.pl.slpbackend.model.SamplingStandard;
import agh.edu.pl.slpbackend.repository.ProductGroupRepository;
import agh.edu.pl.slpbackend.repository.SamplingStandardRepository;
import agh.edu.pl.slpbackend.service.iface.AbstractService;
import agh.edu.pl.slpbackend.service.iface.IModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class SamplingStandardService extends AbstractService implements SamplingStandardMapper {

    private final SamplingStandardRepository samplingStandardRepository;
    private final ProductGroupRepository groupRepository;

    public List<SamplingStandardDto> selectAll() {
        log.info("select all sampling standards");

        final List<SamplingStandard> samplingStandardList = samplingStandardRepository.findAll();
        return samplingStandardList.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public Object insert(IModel model) {
        log.info("insert sampling standard");
        final SamplingStandardDto dto = (SamplingStandardDto) model;
        return samplingStandardRepository.save(toModel(dto));
    }

    @Override
    public Object update(IModel model) {
        log.info("update sampling standard");
        final SamplingStandardDto dto = (SamplingStandardDto) model;
        return samplingStandardRepository.save(toModel(dto));
    }

    @Override
    public void delete(IModel model) {
        log.info("delete sampling standard");
        final SamplingStandardDto dto = (SamplingStandardDto) model;
        SamplingStandard samplingStandard = samplingStandardRepository.findById(dto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        for (ProductGroup group : samplingStandard.getGroups()) {
            group.getSamplingStandards().remove(samplingStandard);
            groupRepository.save(group);
        }
        samplingStandard.getGroups().clear();
        samplingStandardRepository.save(samplingStandard);

        try {
            samplingStandardRepository.deleteById(dto.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DataDependencyException();
        }
    }
}
