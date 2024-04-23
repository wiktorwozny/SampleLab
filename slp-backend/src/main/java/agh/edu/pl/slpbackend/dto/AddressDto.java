package agh.edu.pl.slpbackend.dto;

import agh.edu.pl.slpbackend.model.Client;
import agh.edu.pl.slpbackend.service.iface.IModel;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelClass;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelFieldName;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@ModelClass(Client.class)
public class AddressDto implements IModel, Serializable {
    @Serial
    private static final long serialVersionUID = 8372629405428016723L;

    @Id
    @ModelFieldName("id")
    private Long id;

    @ModelFieldName("street")
    private String street;

    @ModelFieldName("zipCode")
    private String zipCode;

    @ModelFieldName("city")
    private String city;
}
