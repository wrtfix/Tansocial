package com.service.tansocial;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.main.tansocial.GpsManagerCostants;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * hereda de AsyncTask y permitirá enviar los caminos generados por el
 * dispositivo a través de mensajes HTTP con un objeto JSON a un servidor
 * RESTFul
 * 
 * @author Manzanel Mendiola
 * 
 */
public class EnviarCamino extends AsyncTask<Object, Object, String> {

	// Elemento q seran enviados
	private JSONObject camino;
	private Context c;
	private String dirURL;

	public EnviarCamino(JSONObject camino, Context c) {
		super();
		this.camino = camino;
		this.c = c;
		this.dirURL = "http://" + GpsManagerCostants.DIRECCION_IP
				+ ":8080/Servidor/webresources/camino/object";
	}

	@Override
	protected void onPostExecute(String result) {
		Log.d("GuadarCamino", result);
		Toast.makeText(c, result, Toast.LENGTH_LONG).show();

	}

	@Override
	protected String doInBackground(Object... arg0) {

		String result;
		try {
			synchronized (this) {
				this.wait(10000);
			}

//			JSONObject obj = new JSONObject();
//			obj.put("id", camino.getIdCamino());
//			obj.put("nombre", camino.getNombre());
//			obj.put("puntos", camino.getCamino());
//			obj.put("condicionClimatica", camino.getCondicionClimatica());
//			obj.put("fecha", camino.getFecha());
//			obj.put("horaInicio", camino.getHoraInicio());
//			obj.put("horaFin", camino.getHoraFin());
//			obj.put("medioTransporte", camino.getMedioTransporte());
//			obj.put("precioServicio", camino.getPrecioServicio());

			if (postData(camino)) {
				Log.i("EnviarCamino", "Se guarda");
			}

		} catch (Exception e) {
			result = e.toString();
		}
		return "Termino con exito";

	}

	public boolean postData(JSONObject object) {
		boolean result = false;
		HttpClient hc = new DefaultHttpClient();
		String message;

		HttpPost p = new HttpPost(dirURL);

		try {
			message = object.toString();

			p.setEntity(new StringEntity(message));
			p.setHeader("Content-type", "application/json");

			HttpResponse resp = hc.execute(p);
			if (resp != null) {
				if (resp.getStatusLine().getStatusCode() == 204)
					result = true;
			}

			Log.d("GuardarCamino", "" + resp.getStatusLine().getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();

		}

		return result;
	}

}
