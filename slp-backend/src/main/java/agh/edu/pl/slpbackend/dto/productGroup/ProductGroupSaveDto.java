package agh.edu.pl.slpbackend.dto.productGroup;

import agh.edu.pl.slpbackend.service.iface.IModel;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelFieldName;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
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
public class ProductGroupSaveDto implements IModel, Serializable {

    @Serial
    private static final long serialVersionUID = 4567951140930116723L;
    @Id
    @ModelFieldName("id")
    private Long id;
    @ModelFieldName("name")
    @NotBlank(message = "Pole [Nazwa] nie może być puste")
    private String name;
    @ModelFieldName("samplingStandards")
    @NotBlank(message = "Lista [Normy pobrania] nie może być puste")
    private List<Long> samplingStandards;
    @ModelFieldName("assortments")
    private List<Long> assortments;
}
