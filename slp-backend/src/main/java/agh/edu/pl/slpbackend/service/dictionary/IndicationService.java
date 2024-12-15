package agh.edu.pl.slpbackend.service.dictionary;

import agh.edu.pl.slpbackend.dto.AssortmentDto;
import agh.edu.pl.slpbackend.dto.IndicationDto;
import agh.edu.pl.slpbackend.exception.DataDependencyException;
import agh.edu.pl.slpbackend.exception.SampleNotFoundException;
import agh.edu.pl.slpbackend.mapper.IndicationMapper;
import agh.edu.pl.slpbackend.model.Indication;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.repository.IndicationRepository;
import agh.edu.pl.slpbackend.repository.SampleRepository;
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
public class IndicationService extends AbstractService implements IndicationMapper {

    private final IndicationRepository indicationRepository;
    private final SampleRepository sampleRepository;

    public List<IndicationDto> selectAll() {
        List<Indication> indicationList = indicationRepository.findAll();
        return indicationList.stream().map(this::toDto).toList();
    }

    @Override
    public Object insert(IModel model) {

        final IndicationDto indicationDto = (IndicationDto) model;
        final Indication indication = toModel(indicationDto);
        return indicationRepository.save(indication);
    }

    public IndicationDto selectById(final Long indicationId) {
        final Indication indication = indicationRepository.findById(indicationId).orElseThrow();
        return toDto(indication);
    }

    public List<IndicationDto> selectIndicationsForSample(final Long SampleId) {
        final Sample sample = sampleRepository.findById(SampleId)
                .orElseThrow(SampleNotFoundException::new);

        List<Indication> indications = sample.getAssortment().getIndications();

        return indications.stream().map(this::toDto).toList();
    }

    @Override
    public Object update(IModel model) {
        final IndicationDto indicationDto = (IndicationDto) model;
        final Indication indication = toModel(indicationDto);
        return indicationRepository.save(indication);
    }

    @Override
    public void delete(IModel model) {
        final IndicationDto indicationDto = (IndicationDto) model;
        try {
            indicationRepository.deleteById(indicationDto.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DataDependencyException();
        }
    }
}
