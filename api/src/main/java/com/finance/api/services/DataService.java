package com.finance.api.services;

import com.finance.api.models.StockData;

public interface DataService {
    StockData stockData(String ticker, String startDate, String endDate);
}
