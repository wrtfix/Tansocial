package com.tansocial.bean;

/**
 * Bean de Camino
 * @author Jorge Carlos Mendiola
 *
 */
public class Camino  implements java.io.Serializable{

	private static final long serialVersionUID = -2330482232168700153L;
	private String nombre;
	private String camino;
	private int idCamino;

	public int getIdCamino() {
		return idCamino;
	}

	public void setIdCamino(int idCamino) {
		this.idCamino = idCamino;
	}

	public Camino() {

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String firstName) {
		this.nombre = firstName;
	}

	public String getCamino() {
		return camino;
	}

	public void setCamino(String camino) {
		this.camino = camino;
	}

}
