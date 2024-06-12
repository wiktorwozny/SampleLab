package agh.edu.pl.slpbackend.dto.sorting_and_pagination;

import java.util.List;
import java.util.Map;

public record FilterRequest(String fieldName, boolean ascending, int pageNumber, int pageSize, Map<String, List<String>> filters) {
}
