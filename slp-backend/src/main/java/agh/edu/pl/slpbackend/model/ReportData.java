package agh.edu.pl.slpbackend.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class ReportData implements Serializable {

    @Serial
    private static final long serialVersionUID = 854868781531586203L;

    @Id
    @GeneratedValue
    private Long id;

    private String manufacturerName;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Address manufacturerAddress;

    private String manufacturerCountry;

    private String supplierName;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Address supplierAddress;

    private String sellerName;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Address sellerAddress;

    private String recipientName;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Address recipientAddress;

    private LocalDate productionDate;

    private int batchNumber;

    private String batchSizeProd;

    private String batchSizeStorehouse;

    private String samplePacking;

    private String sampleCollectionSite;

    private String sampleCollector;

    private Integer jobNumber;

    private String mechanism;

    private String deliveryMethod;

    private LocalDate collectionDate;

    private String protocolNumber;
}
