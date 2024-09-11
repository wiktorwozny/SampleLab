package agh.edu.pl.slpbackend.dto.filters;

import java.util.List;
import java.util.Map;

public record FilterRequest(String fieldName, boolean ascending, int pageNumber, int pageSize, Filters filters) {
}
