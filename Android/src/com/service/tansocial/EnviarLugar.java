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
 * hereda de AsyncTask y permitirá enviar los lugares generados por el
 * dispositivo a través de mensajes HTTP con un objeto JSON a un servidor
 * RESTFul
 * 
 * @author Mendiola
 * 
 */
public class EnviarLugar extends AsyncTask<Object, Object, String> {

	private String dirURL;
	private JSONObject lugar;
	private Context c;
	
	public EnviarLugar(JSONObject lugar, Context c) {
		super();
		this.lugar=lugar; 
		this.c = c;
		this.dirURL = "http://" + GpsManagerCostants.DIRECCION_IP
				+ ":8080/Servidor/webresources/lugar/mobile";
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
			if (postData(lugar)) {
				Log.i("EnviarLugar", "Se envio con exito!");
			}

		} catch (Exception e) {
			result = e.toString();
		}
		return "Termino con exito";

	}
	/**
	 * Este metodo envia la info al WS
	 * @param object
	 * @return
	 */
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

			Log.d("EnviarLugar", "" + resp.getStatusLine().getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
