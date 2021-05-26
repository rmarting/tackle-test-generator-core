package irs;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.cardinal.util.CardinalException;
import com.ibm.cardinal.util.CardinalLogger;
import com.ibm.cardinal.util.CardinalString;
import com.ibm.cardinal.util.ClusterObjectManager;
import com.ibm.cardinal.util.SerializationUtil;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for BusinessProcess - Generated by Cardinal
 */

@Path("/BusinessProcessService")
public class BusinessProcessService {
    private static final Logger logger = CardinalLogger.getLogger(BusinessProcessService.class);

    // default constructor service
    @POST
    @Path("/BusinessProcess_default_ctor")
    @Produces(MediaType.APPLICATION_JSON) 
    public Response BusinessProcess_default_ctor() {
        BusinessProcess instBusinessProcess = new BusinessProcess();
        String refid = ClusterObjectManager.putObject(instBusinessProcess);
        instBusinessProcess.setKlu__referenceID(refid);
        JsonObject jsonobj = Json
            .createObjectBuilder()
            .add("return_value", refid)
            .build();
        logger.info("[BusinessProcessService] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();
    }



    // health check service
    @GET 
    @Path("/health") 
    @Produces(MediaType.TEXT_HTML) 
    public String getHealth() { 
        logger.info("[BusinessProcess] getHealth() called");
        return "BusinessProcessService::Health OK"; 
    }



    // service for incrementing object reference count
    @POST
    @Path("/incObjectCount")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void incObjectCount(@FormParam("klu__referenceID") String klu__referenceID) {
        logger.info("[BusinessProcessService] incObjectCount() called with ref: "+klu__referenceID);
        ClusterObjectManager.incObjectCount(klu__referenceID);
    }



    // service for decrementing object reference count
    @POST
    @Path("/decObjectCount")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void decObjectCount(@FormParam("klu__referenceID") String klu__referenceID) {
        logger.info("[BusinessProcess] decObjectCount() called with ref: "+klu__referenceID);
        ClusterObjectManager.decObjectCount(klu__referenceID);
    }





    @POST
    @Path("/getAllEmployers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEmployers(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        List<Employer> response;

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        BusinessProcess instBusinessProcess = (BusinessProcess)ClusterObjectManager.getObject(klu__referenceID);

        try {
            response = instBusinessProcess.getAllEmployers();
        }
        catch (Throwable t) {
            String msg = "Call to method getAllEmployers() of BusinessProcess raised exception: "+t.getMessage();
            logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        logger.info("[BusinessProcess] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/genSalarySlip")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void genSalarySlip(
        @FormParam("klu__referenceID") String klu__referenceID,
        @FormParam("irsInst") String irsInst,
        @Context HttpServletResponse servletResponse
    ) {

        
        // convert reference ID(s) stored in "irsInst" to physical/proxy object(s)
        IRS irsInst_fpar = (IRS)SerializationUtil.decodeWithDynamicTypeCheck(irsInst);

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        BusinessProcess instBusinessProcess = (BusinessProcess)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instBusinessProcess.genSalarySlip(irsInst_fpar);
        }
        catch (Throwable t) {
            String msg = "Call to method genSalarySlip() of BusinessProcess raised exception: "+t.getMessage();
            logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/main")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void main(
        @FormParam("args") String args,
        @Context HttpServletResponse servletResponse
    ) {

        
        // deserialize string to primitive array
        String [] args_fpar = (String [])SerializationUtil.decode(args);

        try {
            BusinessProcess.main(args_fpar);
        }
        catch (Throwable t) {
            String msg = "Call to method main() of BusinessProcess raised exception: "+t.getMessage();
            logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/test_irs")
    public void test_irs(
        @Context HttpServletResponse servletResponse
    ) {

        try {
            BusinessProcess.test_irs();
        }
        catch (Throwable t) {
            String msg = "Call to method test_irs() of BusinessProcess raised exception: "+t.getMessage();
            logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/test_employee")
    public void test_employee(
        @Context HttpServletResponse servletResponse
    ) {

        try {
            BusinessProcess.test_employee();
        }
        catch (Throwable t) {
            String msg = "Call to method test_employee() of BusinessProcess raised exception: "+t.getMessage();
            logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/test_employer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test_employer(
        @Context HttpServletResponse servletResponse
    ) {

        List<Employee> response;

        try {
            response = BusinessProcess.test_employer();
        }
        catch (Throwable t) {
            String msg = "Call to method test_employer() of BusinessProcess raised exception: "+t.getMessage();
            logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        logger.info("[BusinessProcess] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/test_employer2")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test_employer2(
        @Context HttpServletResponse servletResponse
    ) {

        Employer response;

        try {
            response = BusinessProcess.test_employer2();
        }
        catch (Throwable t) {
            String msg = "Call to method test_employer2() of BusinessProcess raised exception: "+t.getMessage();
            logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        
        // convert physical/proxy object(s) referenced by "response" to reference ID(s)
        String response_obj = SerializationUtil.encodeWithDynamicTypeCheck(response);
        JsonObject jsonobj = jsonresp.add("return_value", response_obj).build();

        logger.info("[BusinessProcess] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/test_employer3")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test_employer3(
        @Context HttpServletResponse servletResponse
    ) {

        int response;

        try {
            response = BusinessProcess.test_employer3();
        }
        catch (Throwable t) {
            String msg = "Call to method test_employer3() of BusinessProcess raised exception: "+t.getMessage();
            logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }
        JsonObjectBuilder jsonresp = Json.createObjectBuilder();
        JsonObject jsonobj = jsonresp.add("return_value", String.valueOf(response)).build();
        logger.info("[BusinessProcess] Returning JSON object: "+jsonobj.toString());
        return Response
            .status(Response.Status.OK)
            .entity(jsonobj)
            .build();

    }

    @POST
    @Path("/test_salary")
    public void test_salary(
        @Context HttpServletResponse servletResponse
    ) {

        try {
            BusinessProcess.test_salary();
        }
        catch (Throwable t) {
            String msg = "Call to method test_salary() of BusinessProcess raised exception: "+t.getMessage();
            logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/test_irs_salary_set_map")
    public void test_irs_salary_set_map(
        @Context HttpServletResponse servletResponse
    ) {

        try {
            BusinessProcess.test_irs_salary_set_map();
        }
        catch (Throwable t) {
            String msg = "Call to method test_irs_salary_set_map() of BusinessProcess raised exception: "+t.getMessage();
            logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

    @POST
    @Path("/test_2")
    public void test_2(
        @FormParam("klu__referenceID") String klu__referenceID,
        @Context HttpServletResponse servletResponse
    ) {

        // dynamically dispatched method: using reference ID, get object from cluster object manager and
        // call method on the object
        BusinessProcess instBusinessProcess = (BusinessProcess)ClusterObjectManager.getObject(klu__referenceID);

        try {
            instBusinessProcess.test_2();
        }
        catch (Throwable t) {
            String msg = "Call to method test_2() of BusinessProcess raised exception: "+t.getMessage();
            logger.warning(msg);
            throw new WebApplicationException(msg, t, CardinalException.APPLICATION_EXCEPTION);
        }

    }

}