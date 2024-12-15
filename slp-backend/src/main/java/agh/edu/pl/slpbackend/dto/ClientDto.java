package agh.edu.pl.slpbackend.dto;

import agh.edu.pl.slpbackend.model.Address;
import agh.edu.pl.slpbackend.model.Client;
import agh.edu.pl.slpbackend.service.iface.IModel;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelClass;
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

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ModelClass(Client.class)
public class ClientDto implements IModel, Serializable {
    @Serial
    private static final long serialVersionUID = 3905584299085159781L;

    @Id
    @ModelFieldName("id")
    private Long id;

    @ModelFieldName("wijharsCode")
    @NotBlank(message = "Pole [Kod WIJHARS] nie może być puste")
    private String wijharsCode;

    @ModelFieldName("name")
    @NotBlank(message = "Pole [Nazwa] nie może być puste")
    private String name;

    @ModelFieldName("address")
    @NotBlank(message = "Pole [Adres] nie może być puste")
    private Address address;
}
