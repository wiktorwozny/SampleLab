package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.SampleDto;
import agh.edu.pl.slpbackend.model.Sample;

public interface SampleMapper {

    default SampleDto toDto(final Sample model) {
        //@formatter:off
        return SampleDto.builder()
                .id(model.getId())
                .code(model.getCode())
                .client(model.getClient())
                .assortment(model.getAssortment())
                .admissionDate(model.getAdmissionDate())
                .expirationComment(model.getExpirationComment())
                .examinationExpectedEndDate(model.getExaminationExpectedEndDate())
                .expirationDate(model.getExpirationDate())
                .size(model.getSize())
                .state(model.getState())
                .analysis(model.isAnalysis())
                .inspection(model.getInspection())
                .samplingStandard(model.getSamplingStandard())
                .reportData(model.getReportData())
                .progressStatus(model.getProgressStatus())
                .build();
        //@formatter:on

    }

    default Sample toModel(final SampleDto dto) {
        String expirationComment = dto.getExpirationComment().isEmpty() ? "Brak" : dto.getExpirationComment();
        String state = dto.getState().isEmpty() ? "Bez zastrzeżeń" : dto.getState();
        //@formatter:off
        return Sample.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .client(dto.getClient())
                .assortment(dto.getAssortment())
                .admissionDate(dto.getAdmissionDate())
                .expirationComment(expirationComment)
                .expirationDate(dto.getExpirationDate())
                .examinationExpectedEndDate(dto.getExaminationExpectedEndDate())
                .size(dto.getSize())
                .state(state)
                .analysis(dto.isAnalysis())
                .inspection(dto.getInspection())
                .samplingStandard(dto.getSamplingStandard())
                .reportData(dto.getReportData())
                .progressStatus(dto.getProgressStatus())
                .build();
        //@formatter:on

    }
}
