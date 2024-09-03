package agh.edu.pl.slpbackend.dto.filters;

public record FilterRequest(String fieldName, boolean ascending, int pageNumber, int pageSize, Filters filters) {
}
