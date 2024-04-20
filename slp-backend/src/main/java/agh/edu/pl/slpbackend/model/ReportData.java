package agh.edu.pl.slpbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class ReportData implements Serializable {

    @Serial
    private static final long serialVersionUID = 854868781531586203L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
