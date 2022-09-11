package com.finance.api;


import com.finance.api.models.StockData;
import com.finance.api.models.TimeSeries;
import com.finance.api.qualifiers.DataProvider;
import com.finance.api.services.DataProcessingService;
import com.finance.api.services.DataService;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("/data")
public class DataRetrieverController {

    @Inject
    @DataProvider("yahoo")
    private DataService dataService;

    @Inject
    private DataProcessingService<Date, Integer> dataProcessingService;

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
        if(series == null){
            return Response.notAcceptable(null).build();
        }
        return Response.ok(series).build();
    }



}