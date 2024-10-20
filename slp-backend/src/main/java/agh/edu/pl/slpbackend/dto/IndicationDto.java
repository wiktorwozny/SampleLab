package agh.edu.pl.slpbackend.dto;

import agh.edu.pl.slpbackend.model.Indication;
import agh.edu.pl.slpbackend.model.ProductGroup;
import agh.edu.pl.slpbackend.service.iface.IModel;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelClass;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelFieldName;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;


@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ModelClass(Indication.class)
public class IndicationDto implements IModel {

    @Id
    @ModelFieldName("id")
    private Long id;

    @ModelFieldName("name")
    private String name;

    @ModelFieldName("norm")
    private String method;

    @ModelFieldName("unit")
    private String unit;

    @ModelFieldName("laboratory")
    private String laboratory;
}
