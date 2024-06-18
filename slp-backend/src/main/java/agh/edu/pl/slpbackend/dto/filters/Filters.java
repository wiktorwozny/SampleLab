package agh.edu.pl.slpbackend.dto.filters;

import agh.edu.pl.slpbackend.enums.ProgressStatusEnum;

import java.util.List;

public record Filters(List<String> codes, List<String> clients, List<String> groups, List<ProgressStatusEnum> progressStatuses) {
}
