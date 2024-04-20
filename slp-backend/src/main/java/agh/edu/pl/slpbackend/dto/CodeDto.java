package agh.edu.pl.slpbackend.dto;

import agh.edu.pl.slpbackend.model.Code;
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
@ModelClass(Code.class)
public class CodeDto implements IModel, Serializable {
    @Serial
    private static final long serialVersionUID = -5373542437673233154L;

    @Id
    @ModelFieldName("id")
    private String id;

    @ModelFieldName("name")
    private String name;
}
