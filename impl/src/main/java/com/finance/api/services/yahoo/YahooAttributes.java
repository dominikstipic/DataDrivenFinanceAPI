package com.finance.api.services.yahoo;

import java.util.List;

public enum YahooAttributes {
    DATE("date"),
    OPEN("open"),
    HIGH("high"),
    LOW("low"),
    CLOSE("close"),
    ADJ_CLOSE("adj_close"),
    VOLUME("volume");

    private final String value;
    YahooAttributes(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public List<String> getValues(){
        return List.of(
                DATE.getValue(),
                OPEN.getValue(),
                HIGH.getValue(),
                LOW.getValue(),
                CLOSE.getValue(),
                ADJ_CLOSE.getValue(),
                VOLUME.getValue()
        );
    }
}
