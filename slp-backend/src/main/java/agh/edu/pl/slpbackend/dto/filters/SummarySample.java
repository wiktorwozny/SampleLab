package agh.edu.pl.slpbackend.dto.filters;

import agh.edu.pl.slpbackend.enums.ProgressStatus;

import java.time.LocalDate;

public record SummarySample(Long id, String code, String group, String assortment, String clientName,
                            LocalDate admissionDate, ProgressStatus progressStatus) {
}
