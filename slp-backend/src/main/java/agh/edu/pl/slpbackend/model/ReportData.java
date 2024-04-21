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
    @GeneratedValue
    private Long id;

    private String manufacturerName;

    @OneToOne(cascade = CascadeType.ALL)
    private Address manufacturerAddress;

    private String supplierName;

    @OneToOne(cascade = CascadeType.ALL)
    private Address supplierAddress;

    private String sellerName;

    @OneToOne(cascade = CascadeType.ALL)
    private Address sellerAddress;

    private String recipientName;

    @OneToOne(cascade = CascadeType.ALL)
    private Address recipientAddress;

    private Integer jobNumber;

    private String mechanism;

    private String deliveryMethod;
}
