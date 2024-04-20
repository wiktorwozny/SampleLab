package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.ReportDataDto;
import agh.edu.pl.slpbackend.mapper.ReportDataMapper;
import agh.edu.pl.slpbackend.model.ReportData;
import agh.edu.pl.slpbackend.repository.ReportDataRepository;
import agh.edu.pl.slpbackend.service.iface.AbstractService;
import agh.edu.pl.slpbackend.service.iface.IModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor
public class ReportDataService extends AbstractService implements ReportDataMapper {

    private final ReportDataRepository reportDataRepository;

    public List<ReportDataDto> selectAll() {
        final List<ReportData> reportDataList = this.reportDataRepository.findAll();
        return reportDataList.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> insert(IModel model) {
        return null;
    }

    @Override
    public ResponseEntity<?> update(IModel model) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(IModel model) {
        return null;
    }
}
