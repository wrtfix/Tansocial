package com.main.tansocial;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.modelo.tansocial.GuardarCamino;
import com.service.tansocial.EnviarCamino;
import com.service.tansocial.EnviarLugar;


/**
 * El mismo hereda de la clase BroadcastReceiver y es utilizado para actualizar
 * el mapa de la aplicación cuando el service envía a través de un intent un
 * arreglo de puntos. Otra funcionalidad que posee es informar al mapa cuando el
 * servicio es detenido para que sea posible actualizar la configuración de los
 * botones
 * 
 * @author Manzanel Mendiola
 * 
 */
public class Broadcast extends BroadcastReceiver {
	private Double lat;
	private Double lng;
	private boolean monitoreoActivo;
	private OpenMap openMap;
	private GuardarCamino gc;
	private long fechaInicio;
	private ArrayList<String> puntos;

	/**
	 * @param args
	 */

	public Broadcast() {
		lat = 0.0;
		lng = 0.0;
	}

	public Broadcast(OpenMap openMap) {
		lat = 0.0;
		lng = 0.0;
		this.openMap = openMap;
		fechaInicio=System.currentTimeMillis();
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public boolean isMonitoreoActivo() {
		return monitoreoActivo;
	}

	public void setMonitoreoActivo(boolean monitoreoActivo) {
		this.monitoreoActivo = monitoreoActivo;
	}

	public ArrayList<String> getPunto() {
		return puntos;
	}

	public void setPunto(ArrayList<String> punto) {
		this.puntos = punto;
	}

	/**
	 * Este metodo enviar el/los camino/s seleccionados al servidor WSCamino  
	 * @param nombre
	 * @param ip
	 */
	public void enviarCamino(String nombre, String ip) {
		
		Log.i("Broadcast", "Enviando Camino");
		gc = new GuardarCamino(this.openMap, "CAM", null, 1);
		SQLiteDatabase db = gc.getWritableDatabase();

		final List<String> listItems = new ArrayList<String>();
		if (db != null) {
			Cursor c = db.rawQuery("SELECT * FROM Camino where nombre like '"+ nombre + "'", null);
			if (c.getCount() > 0 && c.moveToFirst()) {
				do {
					listItems.add(c.getString(0));

					JSONObject camino = new JSONObject();
					try {
						
						camino.put("idCamino",1);
						camino.put("nombre",c.getString(1));
						camino.put("puntos",c.getString(2));
						camino.put("horaInicio",c.getString(3));
						camino.put("horaFin",c.getString(4));
						camino.put("medioTransporte",c.getString(5));
						camino.put("precioServicio",c.getString(6));
						camino.put("condicionClimatica",c.getString(7));
					
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.e("Broadcast", e.toString());
					}
					
					EnviarCamino cam = new EnviarCamino(camino, this.openMap);
					cam.execute();
				} while (c.moveToNext());
			}
		} else {
			Log.e("Broadcast", "No se encontraron caminos con exito");
		}

		db.close();

	}

	
	/**
	 * Este metodo enviar el/los camino/s seleccionados al servidor WSCamino  
	 * @param nombre
	 * @param ip
	 */
	public void enviarLugar(String nombre) {
		
		Log.i("Broadcast", "Enviando Camino");
		gc = new GuardarCamino(this.openMap, "CAM", null, 1);
		SQLiteDatabase db = gc.getWritableDatabase();

		final List<String> listItems = new ArrayList<String>();
		if (db != null) {
			Cursor c = db.rawQuery("SELECT * FROM Lugar where nombre like '"+ nombre + "'", null);
			if (c.getCount() > 0 && c.moveToFirst()) {
				do {
					listItems.add(c.getString(0));

					JSONObject lugar = new JSONObject();
					try {
						
						lugar.put("idLugar",1);
						lugar.put("nombre",c.getString(1));
						lugar.put("descripcion",c.getString(2));
						lugar.put("direccion",c.getString(3));
						lugar.put("fecha",c.getString(4));
					
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.e("Broadcast", e.toString());
					}
					
					EnviarLugar cam = new EnviarLugar(lugar, this.openMap);
					cam.execute();
					
				} while (c.moveToNext());
			}
		} else {
			Log.e("Broadcast", "No se encontraron caminos con exito");
		}

		db.close();

	}
	
	
	/**
	 * Este metodo guardar el camino en la base de datos
	 * @param nombre
	 * @param clima 
	 * @param costo 
	 * @param transporte 
	 */
	public void guardarCamino(String nombre, String transporte, String costo, String clima) {
		
		if (puntos != null) {

			gc = new GuardarCamino(openMap, "CAM", null, 1);

			SQLiteDatabase db = gc.getWritableDatabase();

			// Si hemos abierto correctamente la base de datos
			if (db != null) {

				// Creamos el registro a insertar como objeto ContentValues
				ContentValues nuevoRegistro = new ContentValues();
				Date date = new Date(fechaInicio);
				nuevoRegistro.put("nombre", nombre);
				nuevoRegistro.put("camino", puntos.toString());
				nuevoRegistro.put("horaInicio", this.fechaInicio);
				nuevoRegistro.put("horaFin", System.currentTimeMillis());
				nuevoRegistro.put("medioTransporte", transporte);
				nuevoRegistro.put("precio", costo);
				nuevoRegistro.put("clima", clima);
				
				// Insertamos el registro en la base de datos
				db.insert("Camino", null, nuevoRegistro);

				db.close();
			}
		} else {
			Log.i("Broadcast", "No hay puntos que se puedan guardar");
		}
	}


	/**
	 * Este metodo guardar el camino en la base de datos
	 * @param nombre
	 */
	public void guardarLugar(String nombre, String descripcion) {
		
		if (puntos != null) {
			
			gc = new GuardarCamino(openMap, "CAM", null, 1);
			
			SQLiteDatabase db = gc.getReadableDatabase(); 
			
			// Si hemos abierto correctamente la base de datos
			if (db != null) {

				// Creamos el registro a insertar como objeto ContentValues
				ContentValues nuevoRegistro = new ContentValues();
				
				nuevoRegistro.put("nombre", nombre);
				nuevoRegistro.put("descripcion", descripcion);
				nuevoRegistro.put("punto", puntos.get(puntos.size()-1));
				nuevoRegistro.put("fecha", System.currentTimeMillis());

				// Insertamos el registro en la base de datos
				db.insert("Lugar", null, nuevoRegistro);
				db.close();
			}
		} else {
			Log.i("Broadcast", "No hay puntos que se puedan guardar");
		}
	}

	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("Broadcast", intent.getAction());
		// si recibe nueva posicion la pinta en el mapa
		if (intent.getAction().equals("NuevaPosicion")) {
			Log.i("Broadcast", "NuevaPosicion");
			puntos = intent.getStringArrayListExtra("punto");
			for (int j = 0; j < puntos.size(); j++) {
				String latgpx = puntos.get(j).split("#")[0];
				String lnggpx = puntos.get(j).split("#")[1];
				openMap.pintarMapa(Double.valueOf(latgpx),
						Double.valueOf(lnggpx));
			}

			intent.getBooleanExtra("Monitoreo", monitoreoActivo);
			// seteo de botones
			if (monitoreoActivo) {
				openMap.setBotonMonitoreo(false);
			} else {
				openMap.setBotonMonitoreo(true);
			}
			// si recibe intent con "StopBroad" setea los botones en el mapa
		} else if (intent.getAction().equals("STOPBroad")) {
			intent.getBooleanExtra("Monitoreo", monitoreoActivo);
			if (!monitoreoActivo) {
				openMap.setBotonMonitoreo(false);
			} else {
				openMap.setBotonMonitoreo(true);
			}

		} else if (intent.getAction().equals("STARTBroad")) {
			Log.i("Broadcast", "recibo camino");
		} else if (intent.getAction().equals("ENVIARBroad")) {
			String nombre = intent.getStringExtra("nombre").toString();
			String ip = intent.getStringExtra("ip").toString();
			this.enviarCamino(nombre, ip);
		} else if (intent.getAction().equals("ENVIARLugarBraod")) {
			String nombre = intent.getStringExtra("nombre").toString();
			String ip = intent.getStringExtra("ip").toString();
			this.enviarLugar(nombre);
		} else if (intent.getAction().equals("GUARDARBroad")) {
			String nombre = intent.getStringExtra("GUARDARBroadNombre");
			String transporte = intent.getStringExtra("GUARDARBroadTransporte");
			String costo = intent.getStringExtra("GUARDARBroadCosto");
			String clima = intent.getStringExtra("GUARDARBroadClima");
			this.guardarCamino(nombre,transporte,costo,clima);
		} else if (intent.getAction().equals("LUGARBroad")) {
			String nombre = intent.getStringExtra("LUGARBroadNombre");
			String descripcion = intent.getStringExtra("LUGARBroadDescrpcion");
			this.guardarLugar(nombre, descripcion);
		}else if (intent.getAction().equals("DetenerNoti")) {
			openMap.dialogoGuardar();
		}else if (intent.getAction().equals("Actual")) {
			openMap.miPosicion();
		
	}
		
		

	}

}
