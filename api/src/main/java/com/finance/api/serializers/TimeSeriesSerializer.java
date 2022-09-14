package com.finance.api.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.finance.api.models.TimeSeries;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TimeSeriesSerializer<D extends Comparable, V extends Number> extends StdSerializer<TimeSeries<D, V>> {
    
    public TimeSeriesSerializer() {
        this(null);
    }
  
    public TimeSeriesSerializer(Class<TimeSeries<D,V>> t) {
        super(t);
    }

    @Override
    public void serialize(
            TimeSeries<D, V> series, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {
        List<String> times = series.getTimes().stream().map(d -> d.toString()).collect(Collectors.toList());
        List<V> values = series.getValues();
        jgen.writeStartObject();
        jgen.writeObjectField("time", times);
        jgen.writeObjectField("values", values);
        jgen.writeEndObject();
    }
}