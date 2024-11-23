package agh.edu.pl.slpbackend.service.dictionary;

import agh.edu.pl.slpbackend.dto.AssortmentDto;
import agh.edu.pl.slpbackend.exception.DataDependencyException;
import agh.edu.pl.slpbackend.mapper.AssortmentMapper;
import agh.edu.pl.slpbackend.mapper.IndicationMapper;
import agh.edu.pl.slpbackend.model.Assortment;
import agh.edu.pl.slpbackend.repository.AssortmentRepository;
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
public class AssortmentService extends AbstractService implements AssortmentMapper, IndicationMapper {
    private final AssortmentRepository assortmentRepository;

    public List<AssortmentDto> selectAll() {
        List<Assortment> codeList = assortmentRepository.findAll();
        return codeList.stream().map(this::toDto).collect(Collectors.toList());
    }


    @Override
    public Object insert(IModel model) {
        final AssortmentDto assortmentDto = (AssortmentDto) model;
        return assortmentRepository.save(toModel(assortmentDto));
    }

    @Override
    public Object update(IModel model) {
        final AssortmentDto assortmentDto = (AssortmentDto) model;
        return assortmentRepository.save(toModel(assortmentDto));
    }

    @Override
    public void delete(IModel model) {
        final AssortmentDto dto = (AssortmentDto) model;
        try {
            assortmentRepository.deleteById(dto.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DataDependencyException();
        }
    }
}
