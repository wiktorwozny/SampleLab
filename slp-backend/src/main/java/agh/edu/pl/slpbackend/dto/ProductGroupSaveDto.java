package agh.edu.pl.slpbackend.dto;

import agh.edu.pl.slpbackend.service.iface.IModel;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelFieldName;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductGroupSaveDto implements IModel, Serializable {

    @Serial
    private static final long serialVersionUID = -6720720397053849550L;

    @Id
    @ModelFieldName("id")
    private Long id;

    @ModelFieldName("name")
    private String name;

    @ModelFieldName("indications")
    private List<Long> indications;

    @ModelFieldName("samplingStandards")
    private List<Long> samplingStandards;
}


