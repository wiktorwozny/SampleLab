package agh.edu.pl.slpbackend.dto;

import agh.edu.pl.slpbackend.enums.ProgressStatusEnum;
import agh.edu.pl.slpbackend.model.*;
import agh.edu.pl.slpbackend.service.iface.IModel;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelClass;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelFieldName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
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
@ModelClass(Sample.class)
public class SampleDto implements IModel, Serializable {

    @Serial
    private static final long serialVersionUID = 3251571983759391816L;

    @Id
    @ModelFieldName("id")
    private Long id;

    @ModelFieldName("code")
    private Code code;

    @ModelFieldName("client")
    private Client client;

    @ModelFieldName("assortment")
    private Assortment assortment;

    @ModelFieldName("admissionDate")
    private LocalDate admissionDate;

    @ModelFieldName("expirationDate")
    private LocalDate expirationDate;

    @ModelFieldName("expirationComment")
    private String expirationComment;

    @ModelFieldName("examinationEndDate")
    private LocalDate examinationExpectedEndDate;

    @ModelFieldName("size")
    private String size;

    @ModelFieldName("state")
    private String state;

    @ModelFieldName("analysis")
    private boolean analysis;

    @ModelFieldName("inspection")
    private Inspection inspection;

    @ModelFieldName("samplingStandard")
    @JsonIgnoreProperties("groups")
    private SamplingStandard samplingStandard;

    @ModelFieldName("reportData")
    private ReportData reportData;

    @ModelFieldName("progressStatus")
    private ProgressStatusEnum progressStatus;
}
