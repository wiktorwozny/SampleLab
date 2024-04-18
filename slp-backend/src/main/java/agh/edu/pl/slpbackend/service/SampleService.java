package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.SampleDto;
import agh.edu.pl.slpbackend.mapper.SampleMapper;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.repository.SampleRepository;
import agh.edu.pl.slpbackend.service.iface.AbstractService;
import agh.edu.pl.slpbackend.service.iface.IModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SampleService extends AbstractService implements SampleMapper {

    private final SampleRepository sampleRepository;

    @Override
    public int insert(IModel model) {

        final SampleDto sampleDto = (SampleDto) model;
        final Sample sample = toModel(sampleDto);
//        return sampleRepository.save(sample);
        return 0; //TODO tu pasuje zeby zwracało 1 jak sie uda 0 jak sie nie uda, albo inaczej abstracta, do przegadania
    }


    @Override
    public int update(IModel model) {
        return 0;
    }

    @Override
    public int delete(IModel model) {
        return 0;
    }
}
