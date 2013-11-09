package com.tansocial.bean;



/**
 * Bean de Persona
 * @author Jorge Carlos Mendiola
 *
 */

public class Persona implements java.io.Serializable{

	private static final long serialVersionUID = -3853139237045132397L;

	private String mail;
	private String nroTelefono;
    private String apellido;
    private String nombre;
    private String domicilio;
    private String nivel;
    private String sueldo;
    private String principalOcupacion;
    private String sexo;
    private String luegarTrabajo;
    private String edad;
    private String contrasenia;
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getNroTelefono() {
		return nroTelefono;
	}
	public void setNroTelefono(String nroTelefono) {
		this.nroTelefono = nroTelefono;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public String getSueldo() {
		return sueldo;
	}
	public void setSueldo(String sueldo) {
		this.sueldo = sueldo;
	}
	public String getPrincipalOcupacion() {
		return principalOcupacion;
	}
	public void setPrincipalOcupacion(String principalOcupacion) {
		this.principalOcupacion = principalOcupacion;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getLuegarTrabajo() {
		return luegarTrabajo;
	}
	public void setLuegarTrabajo(String luegarTrabajo) {
		this.luegarTrabajo = luegarTrabajo;
	}
	public String getEdad() {
		return edad;
	}
	public void setEdad(String edad) {
		this.edad = edad;
	}
	public String getContrasenia() {
		return contrasenia;
	}
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}
    
    
}
