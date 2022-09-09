package com.finance.api;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/hypotesis")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HypothesisTestingController {

    @HEAD
    @Path("/")
    public Response testEndpoint(){
        return Response.ok().build();
    }

    @POST
    @Path("/")
    public Response getResult(List<String> tickers){
        return null;
    }
}