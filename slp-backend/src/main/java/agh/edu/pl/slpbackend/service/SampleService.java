package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.SampleDto;
import agh.edu.pl.slpbackend.dto.filters.FilterRequest;
import agh.edu.pl.slpbackend.dto.filters.FilterResponse;
import agh.edu.pl.slpbackend.dto.filters.SummarySample;
import agh.edu.pl.slpbackend.enums.ProgressStatusEnum;
import agh.edu.pl.slpbackend.exception.SampleNotFoundException;
import agh.edu.pl.slpbackend.mapper.ExaminationMapper;
import agh.edu.pl.slpbackend.mapper.IndicationMapper;
import agh.edu.pl.slpbackend.mapper.ReportDataMapper;
import agh.edu.pl.slpbackend.mapper.SampleMapper;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.repository.SampleRepository;
import agh.edu.pl.slpbackend.repository.Specification.SampleSpecification;
import agh.edu.pl.slpbackend.service.iface.AbstractService;
import agh.edu.pl.slpbackend.service.iface.IModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
        sample.setProgressStatus(ProgressStatusEnum.IN_PROGRESS);
        return sampleRepository.save(sample);
    }

    @Override
    public Object update(IModel model) {
        final SampleDto sampleDto = (SampleDto) model;
        final Sample sample = toModel(sampleDto);
        return sampleRepository.save(sample);
    }

    public Sample updateStatus(final Long id, final ProgressStatusEnum progress) {
        final Sample toUpdate = sampleRepository.findById(id).orElseThrow(SampleNotFoundException::new);
        toUpdate.setProgressStatus(progress);
        return sampleRepository.save(toUpdate);
    }

    @Override
    public void delete(IModel model) {
        final SampleDto sampleDto = (SampleDto) model;
        final Long id = sampleDto.getId();
        sampleRepository.deleteById(id);
    }

    public FilterResponse filter(FilterRequest request) {
        Specification<Sample> specification = hasFieldIn(List.of("code", "id"), request.filters().codes())
                .and(hasFieldIn(List.of("client", "name"), request.filters().clients()))
                .and(hasFieldIn(List.of("assortment", "group", "name"), request.filters().groups()))
                .and(hasFieldIn(List.of("progressStatus"), request.filters().progressStatuses()));

        if (request.fuzzySearch() != null && !request.fuzzySearch().isEmpty()) {
            specification = specification.and(SampleSpecification.fuzzySearch(request.fuzzySearch()));
        }

        Sort.Direction direction = request.ascending() ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(request.pageNumber(), request.pageSize(), Sort.by(direction, request.fieldName()).and(Sort.by(direction, "id")));


        Page<Sample> page = sampleRepository.findAll(specification, pageRequest);
        List<SummarySample> samples = page.stream()
                .map(sample -> new SummarySample(
                        sample.getId(),
                        sample.getCode().getId(),
                        sample.getAssortment().getGroup().getName(),
                        sample.getAssortment().getName(),
                        sample.getClient().getName(),
                        sample.getAdmissionDate(),
                        sample.getProgressStatus()))
                .toList();

        return new FilterResponse(page.getTotalPages(), samples);
    }

//    public FilterResponse fuzzySearch(final String request) {
//
//
//    }

    public long count() {
        return sampleRepository.count();
    }

    private Specification<Sample> hasFieldIn(List<String> attributes, List<?> values) {
        if (values == null || values.isEmpty()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        return (root, query, criteriaBuilder) -> {
            var path = root.get(attributes.get(0));

            for (int i = 1; i < attributes.size(); i++) {
                path = path.get(attributes.get(i));
            }

            return path.in(values);
        };
    }
}
