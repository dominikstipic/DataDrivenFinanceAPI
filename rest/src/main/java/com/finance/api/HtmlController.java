package com.finance.api;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.Paths;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class HtmlController {

    @Context
    private ServletContext servletContext;

    private File basePath;

    @PostConstruct
    private void init(){
        basePath = Paths.get(servletContext.getRealPath("/WEB-INF/classes")).toFile();
    }

    private FileInputStream getPage(String pageName){
        try {
            File f = new File(String.format("%s/%s.html", basePath, pageName));
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @GET
    @Path("/{page}")
    public Response login(@PathParam("page") String name){
        InputStream stream = getPage(name);
        if(stream == null){
            return Response.status(404).build();
        }
        return Response.ok(stream).build();
    }

}
