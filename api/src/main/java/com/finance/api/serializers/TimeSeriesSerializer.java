package com.finance.api.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.finance.api.models.TimeSeries;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TimeSeriesSerializer<D extends Comparable> extends StdSerializer<TimeSeries<D>> {
    
    public TimeSeriesSerializer() {
        this(null);
    }
  
    public TimeSeriesSerializer(Class<TimeSeries<D>> t) {
        super(t);
    }

    @Override
    public void serialize(
            TimeSeries<D> series, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {
        List<String> times = series.getTimes().stream().map(Object::toString).collect(Collectors.toList());
        List<Double> values = series.getValues();
        jgen.writeStartObject();
        jgen.writeObjectField("time", times);
        jgen.writeObjectField("values", values);
        jgen.writeEndObject();
    }
}