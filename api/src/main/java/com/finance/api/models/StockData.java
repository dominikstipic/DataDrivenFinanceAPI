package com.finance.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Time;
import java.util.*;

@Data
@ToString
public class StockData {
    private final String ticker;
    private List<Date> dates = new LinkedList<>();
    private Map<String, List<Number>> timeseries = new HashMap<>();

    public StockData(String ticker, List<String> keys){
        this.ticker = ticker;
        for(String key : keys){
            timeseries.put(key, new LinkedList<>());
        }
    }

    public int size(){
        for(Map.Entry<String, List<Number>> e : timeseries.entrySet()){
            return e.getValue().size();
        }
        return 0;
    }

    public int width(){
        return this.timeseries.keySet().size();
    }

    private boolean checkDateMonotonicity(Date test){
        if(dates.isEmpty())
            return true;
        Date lastDate = dates.get(size()-1);
        int result = test.compareTo(lastDate);
        return result > 0;
    }

    public void addDataPoint(Date date, List<String> keys, List<Number> values){
        if(keys.size() != values.size()){
            throw new IllegalArgumentException("values and key sizes doesn't match!");
        }
        Collection<String> allKeys = timeseries.keySet();
        keys.forEach(k -> {
            if(!allKeys.contains(k)){
                String msg = String.format("Provided key isn't inside %s key set", allKeys);
                throw new IllegalArgumentException(msg);
            }
        });
        if(dates.contains(date)){
            throw new IllegalArgumentException("Provided date already exist in this data cube");
        }
        if(!checkDateMonotonicity(date)){
            throw new IllegalArgumentException("Provided date doesn't monotonically raise!");
        }

        dates.add(date);
        for(int i = 0; i < keys.size(); ++i){
            timeseries.get(keys.get(i)).add(values.get(i));
        }
    }

}
