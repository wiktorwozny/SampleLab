package agh.edu.pl.slpbackend.dto;

import agh.edu.pl.slpbackend.model.Address;
import agh.edu.pl.slpbackend.model.Client;
import agh.edu.pl.slpbackend.model.User;
import agh.edu.pl.slpbackend.service.iface.IModel;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelClass;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelFieldName;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ModelClass(Client.class)
public class ReportDataDto implements IModel, Serializable {

    @Serial
    private static final long serialVersionUID = 854868781531586203L;

    @Id
    @ModelFieldName("id")
    private Long id;

    @ModelFieldName("manufacturerName")
    private String manufacturerName;

    @ModelFieldName("manufacturerAddress")
    private Address manufacturerAddress;

    @ModelFieldName("manufacturerCountry")
    private String manufacturerCountry;

    @ModelFieldName("supplierName")
    private String supplierName;

    @ModelFieldName("supplierAddress")
    private Address supplierAddress;

    @ModelFieldName("sellerName")
    private String sellerName;

    @ModelFieldName("sellerAddress")
    private Address sellerAddress;

    @ModelFieldName("recipientName")
    private String recipientName;

    @ModelFieldName("recipientAddress")
    private Address recipientAddress;

    @ModelFieldName("productionDate")
    private LocalDate productionDate;

    @ModelFieldName("batchNumber")
    private int batchNumber;

    @ModelFieldName("samplePacking")
    private String samplePacking;

    @ModelFieldName("sampleCollectionSite")
    private String sampleCollectionSite;

    @ModelFieldName("sampleCollector")
    private User sampleCollector;

    @ModelFieldName("jobNumber")
    private Integer jobNumber;

    @ModelFieldName("mechanism")
    private String mechanism;

    @ModelFieldName("deliveryMethod")
    private String deliveryMethod;

    private long sampleId;
}
