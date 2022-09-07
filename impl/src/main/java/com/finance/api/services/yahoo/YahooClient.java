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
import java.util.stream.Collectors;

@ProviderClient("yahoo")
public class YahooClient implements RestClient<StockData> {
    private static final String URL = "http://%s:%s";

    private static StockData toStockData(String ticker, Map<String, Map<String, String>> responseBody) throws ParseException {
        List<String> keys = responseBody.keySet().
                stream().
                filter(k -> !k.equals("Date")).
                collect(Collectors.toList());
        StockData data = new StockData(ticker, keys);
        int N = responseBody.get(keys.get(0)).size();
        Map<String, String> dateMap = responseBody.get("Date");
        for(int i = 0; i < N; ++i){
            String iterKey = String.valueOf(i);
            Date date = DateUtils.fromString(dateMap.get(iterKey), DateUtils.FORMAT2.toPattern());
            List<Number> values = keys.stream().
                    map(k -> Double.valueOf(responseBody.get(k).get(iterKey))).
                    collect(Collectors.toList());
            data.addDataPoint(date, keys, values);
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

        StockData data = null;
        try {
            Response<Map<String, Map<String, String>>> response = call.execute();
            data = toStockData(ticker, response.body());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
