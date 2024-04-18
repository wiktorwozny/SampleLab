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
                .expirationDateComment(model.getExpirationDateComment())
                .examinationEndDate(model.getExaminationEndDate())
                .size(model.getSize())
                .state(model.getState())
                .analysis(model.isAnalysis())
                .inspection(model.getInspection())
                .group(model.getGroup())
                .samplingStandard(model.getSamplingStandard())
                .reportData(model.getReportData())
                .build();
        //@formatter:on

    }

    default Sample toModel(final SampleDto dto) {
        //@formatter:off
        return Sample.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .client(dto.getClient())
                .assortment(dto.getAssortment())
                .admissionDate(dto.getAdmissionDate())
                .expirationDateComment(dto.getExpirationDateComment())
                .examinationEndDate(dto.getExaminationEndDate())
                .size(dto.getSize())
                .state(dto.getState())
                .analysis(dto.isAnalysis())
                .inspection(dto.getInspection())
                .group(dto.getGroup())
                .samplingStandard(dto.getSamplingStandard())
                .reportData(dto.getReportData())
                .build();
        //@formatter:on

    }
}
