package com.finance.api.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PortfolioAllocationDTO extends HashMap<String, Double> {

    public List<String> tickers(){
        return new ArrayList<>(keySet());
    }

    public List<Double> weights(List<String> tickers){
        return tickers.stream().map(this::get).collect(Collectors.toList());
    }

}
