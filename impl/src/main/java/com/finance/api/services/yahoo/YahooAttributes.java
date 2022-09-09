package com.finance.api.services.yahoo;

public enum YahooAttributes {
    DATE("Date"),
    OPEN("Open"),
    HIGH("High"),
    LOW("Low"),
    CLOSE("Close"),
    ADJ_CLOSE("Adj Close"),
    VOLUME("Volume");

    private final String value;
    YahooAttributes(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
