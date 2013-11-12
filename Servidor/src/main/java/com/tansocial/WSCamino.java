
package com.tansocial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Query;
import org.hibernate.Session;

import com.sun.jersey.api.view.Viewable;
import com.tansocial.bean.Camino;
import com.tansocial.utils.HibernateUtil;

/** 
 * Example resource class hosted at the URI path "/camino"
 */
@Path("/camino")
@Stateless
public class WSCamino {
    private static ArrayList<String> log = new ArrayList<String>();
	private Logger logger = Logger.getLogger("myLogger");
	
    /** Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     * @return String that will be send back as a response of type "text/plain".
     */
    @GET 
    @Path("/consultarCamino")
    @Produces(MediaType.APPLICATION_JSON)
    public Map getCamino() {
    	Map<String, Object> map = new HashMap<String, Object>();
		map.put("items", log);
		return map;
	}
    
    @POST
	@Path("/json")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/plain")
	public Response createTrackInHTTP(@FormParam("id") String id,
		      @FormParam("firstName") String firstName,
		      @FormParam("camino") String camino) throws IOException {
    	String result ;
    	if (id !=null)
			result = "true" ;
    	else
    		result = "false" ;
		logger.info("ID: "+id+ " Usuario: "+ firstName+" Camino: "+camino);		

		try{
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		 
        session.beginTransaction();
    
        Camino cam = new Camino();
        cam.setIdCamino(Integer.valueOf(id));
        cam.setCamino(camino);
        cam.setNombre(firstName);
        
        
        session.save(cam);
        session.getTransaction().commit();
		}catch (Exception ex){
			logger.error(ex.toString());
		}
		return Response.status(201).entity(result).build();
	}
 
    
    @POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/plain")
    public Response agregarCamino(@FormParam("idCamino") String idCamino,
		      @FormParam("nombre") String nombre,
		      @FormParam("horaInicio") String horaInicio,
		      @FormParam("horaFin") String horaFin,
		      @FormParam("puntos") String puntos,
		      @FormParam("medioTransporte") String medioTransporte,
		      @FormParam("precioServicio") String precioServicio,
		      @FormParam("condicionClimatica") String condicionClimatica
		      ) throws IOException {
    	String result ;
    	if (idCamino !=null)
			result = "El camino con id "+idCamino+" se agrego con exito" ;
    	else
    		result = "Ocurrio algun problema man" ;
		logger.info("ID: "+idCamino+ " Hora de Inicio: "+ horaInicio+" Nombre: "+nombre+"\n");
		log.add(puntos);
		//TODO: Agregar sentencias para insertar un camino en la BD
		return Response.status(201).entity(result).build();
	}

    
    
    @POST
	@Path("/consulta")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response consultaElement(@FormParam("id") String id,
		      @FormParam("firstName") String firstName,
		      @FormParam("camino") String camino) throws IOException {
    	
    	String retorno = "false";
    	
    	if (id !=null)
    		{
    			logger.info("ID: "+id+ " Usuario: "+ firstName+" Camino: "+camino);
    			
    			try{

    			Session session = HibernateUtil.getSessionFactory().openSession();
    			 
    	        Query query=session.getNamedQuery("buscarCamino").setInteger("idCamino", Integer.valueOf(id));
    	        List<?> l= query.list();
    	        
    	        Iterator<?> it=l.iterator();
    	        if (l.size() >0){
    	        	retorno = "";
    	        }
    	        while(it.hasNext())
    	        {
    	            Camino p=(Camino)it.next();
    	            retorno += "Nombre:"+p.getNombre() ;
    	        }
    	        
    	        session.close();
    	        
    			}catch (Exception ex){
    				logger.error(ex.toString());
    			}
    		}
    	
		return Response.status(201).entity(retorno).build();
	}
    @POST
	@Path("/object")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTrackInHTTP(JSONObject path) throws IOException {
    	String result=null;
    	if (path !=null)
    		result = "true";
    	else
    		result = "false";
    	
    	try {
    		logger.info("ID: "+path.getInt("idCamino"));
    		logger.info("Nombre: "+path.getString("nombre"));
    		logger.info("Puntos: "+path.get("puntos").toString());
    		logger.info("Clima: "+path.get("condicionClimatica").toString());
    		logger.info("Transporte: "+path.get("medioTransporte").toString());
    		logger.info("Precio: "+path.get("precioServicio").toString());
    		
    	} catch (JSONException e) {
			e.printStackTrace();
		}
		return Response.status(201).entity(result).build();
	}
    
}
