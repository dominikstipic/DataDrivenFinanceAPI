package com.finance.api;


import com.finance.api.models.StockData;
import com.finance.api.models.TimeSeries;
import com.finance.api.qualifiers.DataProvider;
import com.finance.api.services.DataService;
import com.finance.api.utils.Utils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("/data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataRetrieverController {

    @Inject
    @DataProvider("yahoo")
    private DataService dataService;

    @HEAD
    @Path("/")
    public Response testEndpoint(){
        return Response.ok().build();
    }

    @POST
    @Path("/")
    public Response getDataByPost(List<String> tickers,
                                  @QueryParam("start") String startDate,
                                  @QueryParam("end") String endDate,
                                  @QueryParam("type") String dataType,
                                  @QueryParam("frequency") String frequency){
        List<StockData> data = tickers.
                stream().
                map(t -> dataService.getStockData(t, startDate, endDate)).
                collect(Collectors.toList());

        int nullIndex = Utils.indexOfNull(data);
        if(nullIndex >= 0){
            String msg = String.format("Ticker %s not found", tickers.get(nullIndex));
            return Response.status(404, msg).build();
        }
        return Response.ok(data).build();
    }

    @GET
    @Path("/{ticker}")
    public Response getDataByGet(@PathParam("ticker") String ticker,
                                 @QueryParam("start") String startDate,
                                 @QueryParam("end") String endDate,
                                 @QueryParam("type") String dataType,
                                 @QueryParam("frequency") String frequency){
        StockData data = dataService.getStockData(ticker, startDate, endDate);
        if(data == null)
            return Response.status(404, "Ticker not found").build();
        return Response.ok(data).build();
    }


    @GET
    @Path("/{ticker}/{attribute}")
    public Response getDataByGet(@PathParam("ticker") String ticker,
                                 @PathParam("attribute") String attribute,
                                 @QueryParam("start") String startDate,
                                 @QueryParam("end") String endDate,
                                 @QueryParam("type") String dataType,
                                 @QueryParam("frequency") String frequency){
        TimeSeries<Date, Number> series = dataService.getTimeSeries(ticker, startDate, endDate, attribute);
        if(series == null)
            return Response.status(404, "Data not found, check ticker or passed attribute!").build();
        return Response.ok(series).build();
    }



}