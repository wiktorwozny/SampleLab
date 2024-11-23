package agh.edu.pl.slpbackend.service.dictionary;

import agh.edu.pl.slpbackend.dto.InspectionDto;
import agh.edu.pl.slpbackend.exception.DataDependencyException;
import agh.edu.pl.slpbackend.mapper.InspectionMapper;
import agh.edu.pl.slpbackend.model.Inspection;
import agh.edu.pl.slpbackend.repository.InspectionRepository;
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
public class InspectionService extends AbstractService implements InspectionMapper {

    private final InspectionRepository inspectionRepository;

    public List<InspectionDto> selectAll() {
        List<Inspection> inspectionList = inspectionRepository.findAll();
        return inspectionList.stream().map(this::toDto).collect(Collectors.toList());
    }


    @Override
    public Object insert(IModel model) {
        final InspectionDto dto = (InspectionDto) model;
        return inspectionRepository.save(toModel(dto));
    }

    @Override
    public Object update(IModel model) {
        final InspectionDto dto = (InspectionDto) model;
        return inspectionRepository.save(toModel(dto));
    }

    @Override
    public void delete(IModel model) {
        final InspectionDto dto = (InspectionDto) model;
        try {
            inspectionRepository.deleteById(dto.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DataDependencyException();
        }
    }
}
