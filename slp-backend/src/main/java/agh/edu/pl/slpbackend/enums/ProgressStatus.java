package agh.edu.pl.slpbackend.enums;

public enum ProgressStatus {

    IN_PROGRESS,
    DONE;

    public static ProgressStatus convertEnum(final String value) {
        return switch (value) {
            case "IN_PROGRESS" -> ProgressStatus.IN_PROGRESS;
            case "DONE" -> ProgressStatus.DONE;
            default -> null;
        };
    }
}


