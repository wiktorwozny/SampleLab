package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.ExaminationDto;
import agh.edu.pl.slpbackend.model.Examination;

public interface ExaminationMapper {

    default ExaminationDto toDto(final Examination model) {
        return ExaminationDto.builder()
                .id(model.getId())
                .indication(model.getIndication())
                .sample(model.getSample())
                .signage(model.getSignage())
                .nutritionalValue(model.getNutritionalValue())
                .specification(model.getSpecification())
                .regulation(model.getRegulation())
                .samplesNumber(model.getSamplesNumber())
                .result(model.getResult())
                .startDate(model.getStartDate())
                .endDate(model.getEndDate())
                .methodStatus(model.getMethodStatus())
                .uncertainty(model.getUncertainty())
                .lod(model.getLod())
                .loq(model.getLoq())
                .build();
    }

    default Examination toModel(final ExaminationDto dto) {
        return Examination.builder()
                .id(dto.getId())
                .indication(dto.getIndication())
                .sample(dto.getSample())
                .signage(dto.getSignage())
                .nutritionalValue(dto.getNutritionalValue())
                .specification(dto.getSpecification())
                .regulation(dto.getRegulation())
                .samplesNumber(dto.getSamplesNumber())
                .result(dto.getResult())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .methodStatus(dto.getMethodStatus())
                .uncertainty(dto.getUncertainty())
                .lod(dto.getLod())
                .loq(dto.getLoq())
                .build();
    }
}
