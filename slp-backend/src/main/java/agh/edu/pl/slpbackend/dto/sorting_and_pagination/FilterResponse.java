package agh.edu.pl.slpbackend.dto.sorting_and_pagination;

import java.time.LocalDate;

public record FilterResponse(Long id, String code, String group, String assortment, String clientName, LocalDate admissionDate) {
}
