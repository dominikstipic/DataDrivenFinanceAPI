package com.finance.api.services;

import com.finance.api.models.StochasticProcess;
import com.finance.api.models.StockData;
import com.finance.api.models.TimeSeries;

import java.util.Date;
import java.util.List;

public interface DataService {
    StockData getStockData(String ticker, String startDate, String endDate);

    TimeSeries<Date> getTimeSeries(String ticker, String startDate, String endDate, String type);

    StochasticProcess<Date> getProcess(List<String> ticker,
                                       String startDate,
                                       String endDate,
                                       String type);
}
