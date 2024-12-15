package agh.edu.pl.slpbackend.dto;

import agh.edu.pl.slpbackend.model.Assortment;
import agh.edu.pl.slpbackend.model.Indication;
import agh.edu.pl.slpbackend.model.ProductGroup;
import agh.edu.pl.slpbackend.service.iface.IModel;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelClass;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelFieldName;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class AssortmentDto implements IModel, Serializable {

    @Id
    @ModelFieldName("id")
    private Long id;

    @ModelFieldName("name")
    @NotBlank(message = "Pole [Nazwa] nie może być puste")
    private String name;

    @ModelFieldName("group")
    @NotNull(message = "Pole [Grupa] nie może być puste")
    private ProductGroup group;

    @ModelFieldName("indications")
    @NotEmpty(message = "Lista [Oznaczenia] nie może być puste")
    private List<Indication> indications;

    @ModelFieldName("organolepticMethod")
    @NotBlank(message = "Pole [Metoda organoleptyczna] nie może być puste")
    private String organolepticMethod;
}
