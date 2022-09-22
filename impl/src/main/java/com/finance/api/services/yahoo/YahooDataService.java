package com.finance.api.services.yahoo;

import com.finance.api.models.StochasticProcess;
import com.finance.api.models.TimeSeries;
import com.finance.api.providers.RestClient;
import com.finance.api.qualifiers.ProviderClient;
import com.finance.api.services.DataService;
import com.finance.api.models.StockData;
import com.finance.api.qualifiers.DataProvider;
import com.finance.api.utils.DateUtils;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@DataProvider("yahoo")
public class YahooDataService implements DataService {

    private static final String HOST_IP = "localhost";

    private static final int PORT = 9992;

    @ProviderClient("yahoo")
    @Inject
    private RestClient<StockData> client;

    private StockData getData(String ticker, String startDate, String endDate){
        return client.provide(HOST_IP, PORT, ticker, startDate, endDate);
    }

    @Override
    public StockData getStockData(String ticker, String startDate, String endDate) {
        SimpleDateFormat dateFormat = DateUtils.FORMAT2;
        if(!DateUtils.isValidString(startDate, dateFormat) || !DateUtils.isValidString(endDate, dateFormat)){
            throw new IllegalArgumentException("The date cannot be parsed!");
        }
        startDate = DateUtils.changeFormat(startDate, dateFormat, DateUtils.FORMAT1);
        endDate = DateUtils.changeFormat(endDate, dateFormat, DateUtils.FORMAT1);
        return getData(ticker, startDate, endDate);
    }

    @Override
    public TimeSeries<Date> getTimeSeries(String ticker, String startDate, String endDate, String type) {
        StockData data = getStockData(ticker, startDate, endDate);
        if(data == null || !data.getTimeseries().containsKey(type)){
            return null;
        }
        return data.getTimeseries().get(type);
    }

    @Override
    public StochasticProcess<Date> getProcess(List<String> ticker, String startDate, String endDate, String type) {
        List<TimeSeries<Date>> series = ticker.
                stream().
                map(t -> getTimeSeries(t, startDate, endDate, type)).
                collect(Collectors.toList());
        return new StochasticProcess<>(series);
    }
}
