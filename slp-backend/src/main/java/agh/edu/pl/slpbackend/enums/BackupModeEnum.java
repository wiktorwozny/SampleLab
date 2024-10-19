package agh.edu.pl.slpbackend.enums;


public enum BackupModeEnum {
    FULL_BACKUP,
    CSV,
    DATA_ONLY;


    public static BackupModeEnum convertEnum(final String value) {
        return switch (value) {
            case "FULL_BACKUP" -> BackupModeEnum.FULL_BACKUP;
            case "DATA_ONLY" -> BackupModeEnum.DATA_ONLY;
            case "CSV" -> BackupModeEnum.CSV;
            default -> throw new IllegalArgumentException("Invalid backup mode: " + value);
        };
    }


}


