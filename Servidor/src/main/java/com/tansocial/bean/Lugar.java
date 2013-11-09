package com.tansocial.bean;


/**
 * Bean de Lugar 
 * @author Jorge Carlos Mendiola
 *
 */
public class Lugar  implements java.io.Serializable{
	
	
	private static final long serialVersionUID = -6523874463720667975L;
	private Integer idLugar;
    private String nombre;
    private  String direccion;
    private String descripcion;
	
    public Integer getIdLugar() {
		return idLugar;
	}
	public void setIdLugar(Integer idLugar) {
		this.idLugar = idLugar;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
