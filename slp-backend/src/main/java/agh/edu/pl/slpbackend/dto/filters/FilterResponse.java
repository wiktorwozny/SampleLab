package agh.edu.pl.slpbackend.dto.filters;

import java.time.LocalDate;

public record FilterResponse(Long id, String code, String group, String assortment, String clientName, LocalDate admissionDate) {
}
