package com.finance.api.services;

import com.finance.api.models.StockData;
import com.finance.api.models.TimeSeries;

import java.util.Date;

public interface DataService {
    StockData stockData(String ticker, String startDate, String endDate);

    TimeSeries<Date, Double> assetReturns(String ticker, String startDate, String endDate);
}
