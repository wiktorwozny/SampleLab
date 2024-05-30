package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.ExaminationDto;
import agh.edu.pl.slpbackend.mapper.ExaminationMapper;
import agh.edu.pl.slpbackend.model.Examination;
import agh.edu.pl.slpbackend.repository.ExaminationRepository;
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

    public List<ExaminationDto> selectAll() {
        List<Examination> examinationList = examinationRepository.findAll();
        return examinationList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public void insertExaminationResults(Long examinationId, ExaminationDto updatedExaminationDto) {
        final Examination updatedExamination = toModel(updatedExaminationDto);

        Examination existingExamination = examinationRepository.getReferenceById(examinationId);
        existingExamination.setResult(updatedExamination.getResult());
        existingExamination.setStartDate(updatedExamination.getStartDate());
        existingExamination.setEndDate(updatedExamination.getEndDate());
        existingExamination.setMethodStatus(updatedExamination.getMethodStatus());
        existingExamination.setUncertainty(updatedExamination.getUncertainty());
        existingExamination.setLod(updatedExaminationDto.getLod());
        existingExamination.setLoq(updatedExaminationDto.getLoq());

        examinationRepository.save(existingExamination);
    }

    public List<ExaminationDto> selectExaminationsForSample(final Long sampleId) {
        List<Examination> examinations = examinationRepository.findBySampleId(sampleId);
        return examinations.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ExaminationDto selectById(final Long id) {
        final Examination examination = examinationRepository.findById(id).orElseThrow();
        return toDto(examination);
    }


    @Override
    public Object insert(IModel model) {

        final ExaminationDto examinationDto = (ExaminationDto) model;
        final Examination examination = toModel(examinationDto);
        return examinationRepository.save(examination);
    }

    @Override
    public Object update(IModel model) {
        return null;
    }

    @Override
    public void delete(IModel model) {
        final ExaminationDto examinationDto = (ExaminationDto) model;
        final Examination examination = toModel(examinationDto);
        examinationRepository.deleteById(examination.getId());
    }

}
