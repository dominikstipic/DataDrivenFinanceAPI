package com.finance.api.services.yahoo;

import com.finance.api.models.StockData;
import com.finance.api.providers.RestClient;
import com.finance.api.providers.YahooProvider;
import com.finance.api.qualifiers.ProviderClient;
import com.finance.api.utils.DateUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ProviderClient("yahoo")
public class YahooClient implements RestClient<StockData> {
    private static final String URL = "http://%s:%s";

    private static final String YAHOO_DATE_KEY = "Date";

    private static final Function<String, String> mapper = key -> {
        key = key.toLowerCase();
        key = key.replaceAll("\\s+","_");
        return key;
    };

    private List<String> changeKeys(List<String> keys, Function<String, String> mapper){
        return keys.stream().map(mapper).collect(Collectors.toList());
    }
    private StockData toStockData(String ticker, Map<String, Map<String, String>> responseBody) throws ParseException {
        List<String> keys = responseBody.keySet().
                stream().
                filter(k -> !k.equals(YAHOO_DATE_KEY)).
                collect(Collectors.toList());
        List<String> newKeys = changeKeys(keys, mapper);
        StockData data = new StockData(ticker, newKeys);
        int N = responseBody.get(YAHOO_DATE_KEY).size();
        Map<String, String> dateMap = responseBody.get(YAHOO_DATE_KEY);
        for(int i = 0; i < N; ++i){
            String iterKey = String.valueOf(i);
            Date date = DateUtils.fromString(dateMap.get(iterKey), DateUtils.FORMAT2.toPattern());
            List<Number> values = keys.stream().
                    map(k -> Double.valueOf(responseBody.get(k).get(iterKey))).
                    collect(Collectors.toList());
            data.addDataPoint(date, newKeys, values);
        }
        return data;
    }

    @Override
    public StockData provide(String host, int port, String ticker, String startDate, String endDate) {
        String url = String.format(URL, host, port);
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(url).
                addConverterFactory(JacksonConverterFactory.create()).
                build();
        YahooProvider provider = retrofit.create(YahooProvider.class);
        Call<Map<String, Map<String, String>>> call = provider.getData(
                ticker,
                startDate,
                endDate
        );

        StockData data;
        try {
            Response<Map<String, Map<String, String>>> response = call.execute();
            if (response.code() == 404)
                return null;
            data = toStockData(ticker, response.body());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
