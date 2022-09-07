package com.finance.api.services.yahoo;

import com.finance.api.providers.RestClient;
import com.finance.api.qualifiers.ProviderClient;
import com.finance.api.services.DataService;
import com.finance.api.models.StockData;
import com.finance.api.qualifiers.DataProvider;
import com.finance.api.utils.DateUtils;
import javax.inject.Inject;

@DataProvider("yahoo")
public class YahooDataService implements DataService {

    private static final String HOST_IP = "localhost";

    private static final int PORT = 9992;

    @ProviderClient("yahoo")
    @Inject
    private RestClient<StockData> client;

    ///////////////////////////////////

    private StockData getData(String ticker, String startDate, String endDate){
        return client.provide(HOST_IP, PORT, ticker, startDate, endDate);
    }

    @Override
    public StockData stockData(String ticker, String startDate, String endDate) {
        if(!DateUtils.isValidString(startDate) || !DateUtils.isValidString(endDate)){
            throw new IllegalArgumentException("The date cannot be parsed!");
        }
        StockData data = getData(ticker, startDate, endDate);
        return data;
    }

}
