package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.ExaminationDto;
import agh.edu.pl.slpbackend.enums.ProgressStatus;
import agh.edu.pl.slpbackend.mapper.ExaminationMapper;
import agh.edu.pl.slpbackend.model.Examination;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.repository.ExaminationRepository;
import agh.edu.pl.slpbackend.repository.SampleRepository;
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
public class ExaminationService extends AbstractService implements ExaminationMapper {

    private final ExaminationRepository examinationRepository;
    private final SampleRepository sampleRepository;

    public List<ExaminationDto> selectExaminationsForSample(final Long sampleId) {
        log.info("select examination for sample");
        List<Examination> examinations = examinationRepository.findBySampleId(sampleId);
        return examinations.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ExaminationDto selectById(final Long id) {
        log.info("select examination by id");
        final Examination examination = examinationRepository.findById(id).orElseThrow();
        return toDto(examination);
    }


    @Override
    public Object insert(IModel model) {
        log.info("insert examination");
        final ExaminationDto examinationDto = (ExaminationDto) model;
        final Examination examination = toModel(examinationDto);
        final Object res = examinationRepository.save(examination);

        updateSampleProgress(examination.getSample().getId());

        return res;
    }

    @Override
    public Object update(IModel model) {
        log.info("update examination");
        final ExaminationDto examinationDto = (ExaminationDto) model;
        final Examination examination = toModel(examinationDto);

        final Object res = examinationRepository.save(examination);

        updateSampleProgress(examination.getSample().getId());

        return res;
    }

    private void updateSampleProgress(final long sampleId) {

        final Sample sample = sampleRepository.findById(sampleId).orElseThrow();
        final List<Examination> examinationList = sample.getExaminations();
        boolean completed = false;
        for (Examination examination : examinationList) {
            completed = (examination.getResult() != null && !examination.getResult().isEmpty());
        }

        sample.setProgressStatus(completed ? ProgressStatus.DONE : ProgressStatus.IN_PROGRESS);

        sampleRepository.save(sample);
    }

    @Override
    public void delete(IModel model) {
        log.info("delete examination");
        final ExaminationDto examinationDto = (ExaminationDto) model;
        final Examination examination = toModel(examinationDto);
        examinationRepository.deleteById(examination.getId());
    }

}
