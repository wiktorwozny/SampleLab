package agh.edu.pl.slpbackend.service.dictionary;

import agh.edu.pl.slpbackend.dto.assortment.AssortmentDto;
import agh.edu.pl.slpbackend.dto.assortment.AssortmentSaveDto;
import agh.edu.pl.slpbackend.mapper.AssortmentMapper;
import agh.edu.pl.slpbackend.mapper.IndicationMapper;
import agh.edu.pl.slpbackend.model.Assortment;
import agh.edu.pl.slpbackend.model.ProductGroup;
import agh.edu.pl.slpbackend.repository.AssortmentRepository;
import agh.edu.pl.slpbackend.repository.IndicationRepository;
import agh.edu.pl.slpbackend.repository.ProductGroupRepository;
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
public class AssortmentService extends AbstractService implements AssortmentMapper, IndicationMapper {
    private final AssortmentRepository assortmentRepository;
    private final IndicationRepository indicationRepository;
    private final ProductGroupRepository productGroupRepository;

    public List<AssortmentDto> selectAll() {
        List<Assortment> codeList = assortmentRepository.findAll();
        return codeList.stream().map(this::toDto).collect(Collectors.toList());
    }


    @Override
    public Object insert(IModel model) {
        final AssortmentSaveDto dto = (AssortmentSaveDto) model;
        final Assortment assortment = toModel(createObjectToSave(dto, false));
        return assortmentRepository.save(assortment);
    }

    @Override
    public Object update(IModel model) {
        final AssortmentSaveDto dto = (AssortmentSaveDto) model;
        return assortmentRepository.save(toModel(createObjectToSave(dto, true)));
    }

    @Override
    public void delete(IModel model) {
        final AssortmentDto dto = (AssortmentDto) model;
        assortmentRepository.deleteById(dto.getId());
    }

    private AssortmentDto createObjectToSave(AssortmentSaveDto dto, final boolean isEdit) {

        final AssortmentDto dtoToSave = new AssortmentDto();
        dtoToSave.setId(dto.getId());
        dtoToSave.setName(dto.getName());
        dtoToSave.setOrganolepticMethod(dto.getOrganolepticMethod());


        if (isEdit) {
            final Assortment assortment = assortmentRepository.findById(dto.getId()).orElseThrow();
            assortment.getIndications().forEach(indication -> indicationRepository.deleteById(indication.getId()));
        }

        final ProductGroup group = productGroupRepository.findById(dto.getGroup()).orElseThrow();
        dtoToSave.setGroup(group);

//        dto.getIndications().forEach(indication -> indication.setId(null));

        dtoToSave.setIndications(dto.getIndications());
        return dtoToSave;
    }

}
