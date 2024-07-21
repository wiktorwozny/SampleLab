package agh.edu.pl.slpbackend.dto.filters;

import java.util.List;

public record FilterResponse(int totalPages, List<SummarySample> samples) {
}
