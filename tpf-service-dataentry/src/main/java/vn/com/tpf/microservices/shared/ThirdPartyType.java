package vn.com.tpf.microservices.shared;

import org.springframework.util.StringUtils;

/**
 * Note: Just follow old version
 */
public enum ThirdPartyType {

    DIGITEXX   ("DIGI-TEXX", 1),
    SGBPO   ("SGBPO", 2),
    INHOUSE ("IN-HOUSE", 3);


    private String name;
    private int code;

    ThirdPartyType(String name, int code) {
        this.code = code;
        this.name = name;
    }

    public static ThirdPartyType fromName(String name) {
        if (StringUtils.hasText(name)) {
            for (ThirdPartyType ins : ThirdPartyType.values()) {
                if (ins.name.equalsIgnoreCase(name)) {
                    return ins;
                }
            }
        }
        return null;
    }

    public static int fromNameToCode(String name) {
        if (StringUtils.hasText(name)) {
            for (ThirdPartyType ins : ThirdPartyType.values()) {
                if (ins.name.equalsIgnoreCase(name)) {
                    return ins.code;
                }
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }
}
