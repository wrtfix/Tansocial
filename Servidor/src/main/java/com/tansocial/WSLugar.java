package com.tansocial;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/** 
 * Example resource class hosted at the URI path "/lugar"
 */
@Path("/lugar")
@Stateless
public class WSLugar {
	private Logger logger = Logger.getLogger("myLogger");
	
    @POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/plain")
	public Response agregarPersona(@FormParam("idLugar") String idLugar,
		      @FormParam("nombre") String nombre,
		      @FormParam("direccion") String direccion,
		      @FormParam("descripcion") String descripcion,
		      @FormParam("fecha") String fecha
			) throws IOException {

    	String result ;
    	if (idLugar !=null)
			result = "El usuario "+ nombre +" se agrego con exito el usuario" ;
    	else
    		result = "No se pudo agregar el usuario" ;
		
    	logger.info("idLugar: "+idLugar+ " nombre: "+ nombre +" direccion: "+direccion);
		
    	//TODO: Agregar sentencias para insertar un lugar en la BD
		return Response.status(201).entity(result).build();
	}
    
    @POST
	@Path("/mobile")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTrackInHTTP(JSONObject path) throws IOException {
    	String result=null;
    	
    	if (path !=null)
			result = "Se agrego con exito el lugar" ;
    	else
    		result = "No se pudo agregar el lugar" ;
    	
    	try {
    		logger.info("ID: "+path.getInt("idLugar"));
    		logger.info("Nombre: "+path.getString("nombre"));
    		logger.info("Descipcion: "+path.getString("descripcion"));
    		logger.info("Direccion: "+path.getString("direccion"));
    		logger.info("Fecha: "+path.get("fecha").toString());
        	
    		//TODO: Agregar sentencias para insertar un lugar en la BD
    	} catch (JSONException e) {
			e.printStackTrace();
		}
		return Response.status(201).entity(result).build();
	}
    
}
