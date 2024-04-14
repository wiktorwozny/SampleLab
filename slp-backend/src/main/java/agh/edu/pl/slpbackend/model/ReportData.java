package agh.edu.pl.slpbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ReportData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String manufacturerName;

    @OneToOne
    private Address manufacturerAddress;

    private String supplierName;

    @OneToOne
    private Address supplierAddress;

    private String sellerName;

    @OneToOne
    private Address sellerAddress;

    private String recipientName;

    @OneToOne
    private Address recipientAddress;

    private Integer jobNumber;

    private String mechanism;

    private String deliveryMethod;
}
