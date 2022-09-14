package com.finance.api.providers;

public interface RestClient<T> {
    T provide(String host, int port, String ticker, String startDate, String endDate);
}
