package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.ReportDataDto;
import agh.edu.pl.slpbackend.dto.SampleDto;
import agh.edu.pl.slpbackend.exception.SampleNotFoundException;
import agh.edu.pl.slpbackend.mapper.ReportDataMapper;
import agh.edu.pl.slpbackend.model.ReportData;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.repository.ReportDataRepository;
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
public class ReportDataService extends AbstractService implements ReportDataMapper {

    private final ReportDataRepository reportDataRepository;
    private final SampleRepository sampleRepository;

    public List<ReportDataDto> selectAll() {
        final List<ReportData> reportDataList = this.reportDataRepository.findAll();
        return reportDataList.stream().map(this::toDto).collect(Collectors.toList());
    }
    public ReportDataDto selectBySampleId(Long sampleId) {
        final Sample sample = sampleRepository
                .findById(sampleId)
                .orElseThrow(SampleNotFoundException::new);
        final ReportData reportData = sample.getReportData();
        if(reportData == null){
            return null;
        }
        return toDto(reportData);
    }
//    @Override
//    public ResponseEntity<?> insert(IModel model) {
//        final ReportDataDto reportDataDto = (ReportDataDto) model;
//        final ReportData reportData = toModel(reportDataDto);
//        final ReportData saveResult = reportDataRepository.save(reportData);
//
//        return new ResponseEntity<>(saveResult, HttpStatus.CREATED);
//    }

    @Override
    public Object insert(IModel model) {

        final ReportDataDto reportDataDto = (ReportDataDto) model;
        Sample sample = sampleRepository.findById(reportDataDto.getSampleId())
                .orElseThrow(SampleNotFoundException::new);

        final ReportData reportData = toModel(reportDataDto);

        sample.setReportData(reportData);
        return sampleRepository.save(sample);
    }

    @Override
    public Object update(IModel model) {
        final ReportDataDto reportDataDto = (ReportDataDto) model;
        final ReportData reportData = toModel(reportDataDto);
        return reportDataRepository.save(reportData);
    }

    @Override
    public void delete(IModel model) {
        final ReportDataDto reportDataDto = (ReportDataDto) model;
        final Long id = reportDataDto.getId();
        Sample sample = sampleRepository.findByReportDataId(id)
                        .orElseThrow(SampleNotFoundException::new);
        sample.setReportData(null);
        reportDataRepository.deleteById(id);
    }
}
