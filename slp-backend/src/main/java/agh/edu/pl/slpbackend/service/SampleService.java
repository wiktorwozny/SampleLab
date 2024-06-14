package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.SampleDto;
import agh.edu.pl.slpbackend.dto.filters.FilterRequest;
import agh.edu.pl.slpbackend.dto.filters.FilterResponse;
import agh.edu.pl.slpbackend.exception.SampleNotFoundException;
import agh.edu.pl.slpbackend.mapper.ExaminationMapper;
import agh.edu.pl.slpbackend.mapper.IndicationMapper;
import agh.edu.pl.slpbackend.mapper.ReportDataMapper;
import agh.edu.pl.slpbackend.mapper.SampleMapper;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.repository.SampleRepository;
import agh.edu.pl.slpbackend.service.iface.AbstractService;
import agh.edu.pl.slpbackend.service.iface.IModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class SampleService extends AbstractService implements SampleMapper, IndicationMapper, ExaminationMapper, ReportDataMapper {

    private final SampleRepository sampleRepository;


    public List<SampleDto> selectAll() {
        List<Sample> sampleList = sampleRepository.findAll();
        return sampleList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public SampleDto selectOne(Long id) {
        final Sample sample = sampleRepository.findById(id)
                .orElseThrow(SampleNotFoundException::new);
        return toDto(sample);
    }

    @Override
    public Object insert(IModel model) {

        final SampleDto sampleDto = (SampleDto) model;
        final Sample sample = toModel(sampleDto);
        return sampleRepository.save(sample);

    }

    @Override
    public Object update(IModel model) {
        final SampleDto sampleDto = (SampleDto) model;
        final Sample sample = toModel(sampleDto);
        return sampleRepository.save(sample);
    }

    @Override
    public void delete(IModel model) {
        final SampleDto sampleDto = (SampleDto) model;
        final Long id = sampleDto.getId();
        sampleRepository.deleteById(id);
    }

    public List<FilterResponse> filter(FilterRequest request) {
        Specification<Sample> specification = hasFieldIn("code", "id", request.filters().codes())
                .and(hasFieldIn("client", "name", request.filters().clients()))
                .and(hasFieldIn("group", "name", request.filters().groups()));

        Sort.Direction direction = request.ascending() ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(request.pageNumber(), request.pageSize(), Sort.by(direction, request.fieldName()));

        return sampleRepository.findAll(specification, pageRequest).stream()
                .map(sample -> new FilterResponse(
                        sample.getId(),
                        sample.getCode().getId(),
                        sample.getGroup().getName(),
                        sample.getAssortment(),
                        sample.getClient().getName(),
                        sample.getAdmissionDate()))
                .toList();
    }

    public long count() {
        return sampleRepository.count();
    }

    private Specification<Sample> hasFieldIn(String fieldName, String attribute, List<String> values) {
        if (values == null || values.isEmpty()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) -> root.get(fieldName).get(attribute).in(values);
    }
}
