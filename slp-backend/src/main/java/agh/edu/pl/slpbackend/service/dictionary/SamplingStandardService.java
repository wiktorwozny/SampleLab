package agh.edu.pl.slpbackend.service.dictionary;

import agh.edu.pl.slpbackend.dto.SamplingStandardDto;
import agh.edu.pl.slpbackend.mapper.SamplingStandardMapper;
import agh.edu.pl.slpbackend.model.SamplingStandard;
import agh.edu.pl.slpbackend.repository.SamplingStandardRepository;
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
public class SamplingStandardService extends AbstractService implements SamplingStandardMapper {

    private final SamplingStandardRepository samplingStandardRepository;

    public List<SamplingStandardDto> selectAll() {
        final List<SamplingStandard> samplingStandardList = samplingStandardRepository.findAll();
        return samplingStandardList.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public Object insert(IModel model) {
        final SamplingStandardDto dto = (SamplingStandardDto) model;
        return samplingStandardRepository.save(toModel(dto));
    }

    @Override
    public Object update(IModel model) {
        final SamplingStandardDto dto = (SamplingStandardDto) model;
        return samplingStandardRepository.save(toModel(dto));
    }

    @Override
    public void delete(IModel model) {
        final SamplingStandardDto dto = (SamplingStandardDto) model;
        samplingStandardRepository.deleteById(dto.getId());
    }
}