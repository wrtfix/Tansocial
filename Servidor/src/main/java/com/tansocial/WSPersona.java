package com.tansocial;

import java.io.IOException;
import java.util.ArrayList;

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
 * Example resource class hosted at the URI path "/persona"
 */
@Path("/persona")
@Stateless
public class WSPersona {
    private ArrayList<String> log = new ArrayList<String>();
	private Logger logger = Logger.getLogger("myLogger");
	
    @POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/plain")
	public Response agregarPersona(@FormParam("mail") String mail,
		      @FormParam("nroTelefono") String nroTelefono,
		      @FormParam("apellido") String apellido,
		      @FormParam("nombre") String nombre,
		      @FormParam("domicilio") String domicilio,
		      @FormParam("nivelEstudio") String nivel,
		      @FormParam("sueldo") String sueldo,
		      @FormParam("principalOcupacion") String principalOcupacion,
		      @FormParam("sexo") String sexo,
		      @FormParam("edad") String edad,
		      @FormParam("contrasenia") String contrasenia
			) throws IOException {

    	String result ;
    	if (mail !=null)
			result = "El usuario "+ nombre +" se agrego con exito el usuario" ;
    	else
    		result = "No se pudo agregar el usuario" ;
		
    	logger.info("mail: "+mail+ " nroTelefono: "+ nroTelefono+" apellido: "+apellido);
		
    	//TODO: Agregar sentencias para insertar una persona en la BD
		return Response.status(201).entity(result).build();
	}
    
    @POST
	@Path("/mobile")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTrackInHTTP(JSONObject path) throws IOException {
    	String result=null;
    	
    	if (path !=null)
			result = "Se agrego con exito el usuario" ;
    	else
    		result = "No se pudo agregar el usuario" ;
    	
    	try {
    		
    		logger.info("Mail: "+path.get("mail"));
    		logger.info("Telefono: "+path.get("nroTelefono"));
    		logger.info("Nombre: "+path.get("nombre"));
    		logger.info("Apellido: "+path.get("apellido"));
    		logger.info("Domicilio: "+path.get("domicilio"));
    		logger.info("Estudio: "+path.get("nivelEstudio"));
    		logger.info("Sueldo: "+path.get("sueldo"));
    		logger.info("Ocupacion: "+path.get("principalOcupacion"));
    		logger.info("Sexo: "+path.get("sexo"));
    		logger.info("Edad: "+path.get("edad"));
    		logger.info("Contraseña: "+path.get("contrasenia"));
    		
    	} catch (JSONException e) {
			e.printStackTrace();
		}
		return Response.status(201).entity(result).build();
	}
 
    @POST
	@Path("/validar")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validarUsuario(JSONObject path) throws IOException {
    	String result=null;
    	Response res = Response.status(201).entity(result).build();;
    	if (path !=null)
			result = "Se agrego con exito el usuario" ;
    	else
    		result = "No se pudo agregar el usuario" ;
    	
    	try {
    		
    		logger.info("Nombre: "+path.get("nombre"));
    		logger.info("Contraseña: "+path.get("contrasenia"));
    		//FIXME Agregar consulta a la base de datos para validar usuario
    		if (path.get("contrasenia").equals("juli")){
    			res = Response.status(200).entity(result).build();
    		}
    	} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}
 
    
}
