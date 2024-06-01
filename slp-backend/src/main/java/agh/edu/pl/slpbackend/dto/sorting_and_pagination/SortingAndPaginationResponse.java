package agh.edu.pl.slpbackend.dto.sorting_and_pagination;

import java.time.LocalDate;

public record SortingAndPaginationResponse(Long id, String code, LocalDate admissionDate,LocalDate expirationDate, String clientName) {
}
