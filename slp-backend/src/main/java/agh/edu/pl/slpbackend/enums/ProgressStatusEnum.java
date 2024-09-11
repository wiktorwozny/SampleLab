package agh.edu.pl.slpbackend.enums;

public enum ProgressStatusEnum {

    TODO,
    IN_PROGRESS,
    DONE;

    public static ProgressStatusEnum convertEnum(final String value) {
        return switch (value) {
            case "TODO" -> ProgressStatusEnum.TODO;
            case "IN_PROGRESS" -> ProgressStatusEnum.IN_PROGRESS;
            case "DONE" -> ProgressStatusEnum.DONE;
            default -> null;
        };
    }
}


