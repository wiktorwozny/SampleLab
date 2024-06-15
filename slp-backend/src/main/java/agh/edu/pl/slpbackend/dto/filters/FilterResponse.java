package agh.edu.pl.slpbackend.dto.filters;

import java.time.LocalDate;
import java.util.List;

public record FilterResponse(int totalPages, List<SummarySample> samples) {
}
