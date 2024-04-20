package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.dto.CodeDto;
import agh.edu.pl.slpbackend.mapper.CodeMapper;
import agh.edu.pl.slpbackend.model.Code;
import agh.edu.pl.slpbackend.repository.CodeRepository;
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
public class CodeService extends AbstractService implements CodeMapper {

    private final CodeRepository codeRepository;

    public List<CodeDto> selectAll() {
        List<Code> codeList = codeRepository.findAll();
        return codeList.stream().map(this::toDto).collect(Collectors.toList());
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
