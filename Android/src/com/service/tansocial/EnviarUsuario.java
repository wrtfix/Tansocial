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
import android.view.View;
import android.widget.Toast;

/**
 * Hereda de AsyncTask y permitirá regitrar usuarios de la aplicacion a traves del 
 * dispositivo con el envio HTTP de un objeto JSON a un servidor
 * RESTFul
 * 
 * @author Mendiola
 * 
 */
public class EnviarUsuario extends AsyncTask<Object, Object, String> {
	private JSONObject usuario;
	private String dirURL;
	private Context c;

	public EnviarUsuario(JSONObject usuario,View v) {
		
		super();
		this.usuario = usuario;
	    this.c = v.getContext();
		this.dirURL = "http://" + GpsManagerCostants.DIRECCION_IP
				+ ":8080/Servidor/webresources/persona/mobile";
	}

	@Override
	protected void onPostExecute(String result) {
		Log.d("RegistrarUsuario", result);
		Toast.makeText(c, result, Toast.LENGTH_LONG).show();

	}

	@Override
	protected String doInBackground(Object... arg0) {

		String result;
		try {
			synchronized (this) {
				this.wait(10000);
			}

			if (postData(usuario)) {
				Log.i("RegistrarUsuario", "Se guarda");
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

			Log.d("RegistrarUsuario", "" + resp.getStatusLine().getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

}
