package agh.edu.pl.slpbackend.dto.assortment;

import agh.edu.pl.slpbackend.model.Assortment;
import agh.edu.pl.slpbackend.service.iface.IModel;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelClass;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelFieldName;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ModelClass(Assortment.class)
public class AssortmentSaveDto implements IModel, Serializable {

    @Id
    @ModelFieldName("id")
    private Long id;

    @ModelFieldName("name")
    private String name;

    @ModelFieldName("group")
    private Long group;

    @ModelFieldName("indications")
    private List<Long> indications;
}