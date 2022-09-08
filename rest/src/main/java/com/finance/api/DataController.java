package com.finance.api;


import com.finance.api.models.StockData;
import com.finance.api.qualifiers.DataProvider;
import com.finance.api.services.DataService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataController {

    @Inject
    @DataProvider("yahoo")
    private DataService dataService;

    @HEAD
    @Path("/")
    public Response testEndpoint(){
        return Response.ok().build();
    }

    @GET
    @Path("/")
    public Response getData(@QueryParam("ticker") String ticker,
                            @QueryParam("start") String startDate,
                            @QueryParam("end") String endDate,
                            @QueryParam("type") String dataType,
                            @QueryParam("frequency") String frequency){
        System.out.println(ticker);
        StockData data = dataService.stockData(ticker, startDate, endDate);
        return Response.ok(data).build();
    }

    @POST
    @Path("/")
    public Response getData(List<String> tickers,
                            @QueryParam("start") String startDate,
                            @QueryParam("end") String endDate,
                            @QueryParam("type") String dataType,
                            @QueryParam("frequency") String frequency){
        List<StockData> data = tickers.
                stream().
                map(t -> dataService.stockData(t, startDate, endDate)).
                collect(Collectors.toList());
        return Response.ok(data).build();
    }
}