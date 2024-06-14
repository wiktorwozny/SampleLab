package agh.edu.pl.slpbackend.dto.filters;

import java.util.List;

public record Filters(List<String> codes, List<String> clients, List<String> groups) {
}
