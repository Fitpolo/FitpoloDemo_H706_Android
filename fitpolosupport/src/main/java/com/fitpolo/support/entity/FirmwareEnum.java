package com.fitpolo.support.entity;


public enum FirmwareEnum {
    H706("0006", 9, "0006_0001_0009_DFU.zip"),
    ;


    private String header;
    private int lastestVersion;
    private String firmwareName;

    FirmwareEnum(String header, int lastestVersion, String firmwareName) {
        this.header = header;
        this.lastestVersion = lastestVersion;
        this.firmwareName = firmwareName;
    }

    public String getHeader() {
        return header;
    }

    public int getLastestVersion() {
        return lastestVersion;
    }

    public String getFirmwareName() {
        return firmwareName;
    }

    public static FirmwareEnum fromHeader(String header) {
        for (FirmwareEnum firwmareEnum : FirmwareEnum.values()) {
            if (firwmareEnum.getHeader().equals(header)) {
                if ("0006".equals(header)) {
                    return H706;
                }
                return firwmareEnum;
            }
        }
        return null;
    }

    public static FirmwareEnum fromLastestVersion(int lastestVersion) {
        for (FirmwareEnum firwmareEnum : FirmwareEnum.values()) {
            if (firwmareEnum.getLastestVersion() == lastestVersion) {
                return firwmareEnum;
            }
        }
        return null;
    }

    public static FirmwareEnum fromFirmwareName(String fromFirmwareName) {
        for (FirmwareEnum firwmareEnum : FirmwareEnum.values()) {
            if (firwmareEnum.getFirmwareName().equals(fromFirmwareName)) {
                return firwmareEnum;
            }
        }
        return null;
    }
}
