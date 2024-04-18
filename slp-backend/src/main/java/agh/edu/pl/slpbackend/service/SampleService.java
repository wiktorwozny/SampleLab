package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.SampleDto;
import agh.edu.pl.slpbackend.mapper.SampleMapper;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.repository.SampleRepository;
import agh.edu.pl.slpbackend.service.iface.AbstractService;
import agh.edu.pl.slpbackend.service.iface.IModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SampleService extends AbstractService implements SampleMapper {

    private final SampleRepository sampleRepository;

    @Override
    public ResponseEntity<Sample> insert(IModel model) {

        final SampleDto sampleDto = (SampleDto) model;
//        final Sample sample = toModel(sampleDto);
//        final Sample saveResult = sampleRepository.save(sample);
//
//        return new ResponseEntity<>(saveResult, HttpStatus.CREATED);

        return new ResponseEntity<>(sampleRepository.save(toModel(sampleDto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Sample> update(IModel model) {
        return null;
    }

    @Override
    public ResponseEntity<Sample> delete(IModel model) {
        return null;
    }


}
