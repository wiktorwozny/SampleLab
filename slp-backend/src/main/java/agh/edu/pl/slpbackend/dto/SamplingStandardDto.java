package agh.edu.pl.slpbackend.dto;

import agh.edu.pl.slpbackend.model.ProductGroup;
import agh.edu.pl.slpbackend.model.SamplingStandard;
import agh.edu.pl.slpbackend.service.iface.IModel;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelClass;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelFieldName;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@ModelClass(SamplingStandard.class)
public class SamplingStandardDto implements IModel, Serializable {
    @Serial
    private static final long serialVersionUID = -5564403812760546136L;

    @Id
    @ModelFieldName("id")
    private Long id;

    @ModelFieldName("name")
    private String name;

    @ModelFieldName("groups")
    private List<ProductGroup> groups;
}
