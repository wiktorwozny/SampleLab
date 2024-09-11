package agh.edu.pl.slpbackend.dto;

import agh.edu.pl.slpbackend.model.Examination;
import agh.edu.pl.slpbackend.model.Indication;
import agh.edu.pl.slpbackend.model.Sample;
import agh.edu.pl.slpbackend.service.iface.IModel;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelClass;
import agh.edu.pl.slpbackend.service.iface.annotation.ModelFieldName;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ModelClass(Examination.class)
public class ExaminationDto implements IModel {

    @Id
    @ModelFieldName("id")
    private Long id;

    @ModelFieldName("indication")
    private Indication indication;

    @ModelFieldName("sample")
    private Sample sample;

    @ModelFieldName("signage")
    private String signage;

    @ModelFieldName("nutritionalValue")
    private String nutritionalValue;

    @ModelFieldName("specification")
    private String specification;

    @ModelFieldName("regulation")
    private String regulation;

    @ModelFieldName("samplesNumber")
    private int samplesNumber;

    @ModelFieldName("result")
    private String result;

    @ModelFieldName("startDate")
    private LocalDate startDate;

    @ModelFieldName("endDate")
    private LocalDate endDate;

    @ModelFieldName("methodStatus")
    private String methodStatus;

    @ModelFieldName("uncertainty")
    private float uncertainty;

    @ModelFieldName("lod")
    private float lod;

    @ModelFieldName("loq")
    private float loq;

}
