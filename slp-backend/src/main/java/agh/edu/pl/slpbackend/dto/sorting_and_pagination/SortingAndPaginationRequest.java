package agh.edu.pl.slpbackend.dto.sorting_and_pagination;

public record SortingAndPaginationRequest(String fieldName, boolean ascending, int pageNumber, int pageSize) {
}
