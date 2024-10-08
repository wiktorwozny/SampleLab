package agh.edu.pl.slpbackend.dto;

import agh.edu.pl.slpbackend.model.Inspection;
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

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ModelClass(Inspection.class)
public class InspectionDto implements IModel, Serializable {

    @Serial
    private static final long serialVersionUID = -1144966894315267793L;

    @Id
    @ModelFieldName("id")
    private Long id;

    @ModelFieldName("name")
    private String name;
}
