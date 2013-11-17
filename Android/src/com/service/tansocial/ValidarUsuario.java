package com.service.tansocial;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.main.tansocial.GpsManagerCostants;

/**
 * hereda de AsyncTask y permitirá enviar los caminos generados por el
 * dispositivo a través de mensajes HTTP con un objeto JSON a un servidor
 * RESTFul
 * 
 * @author Manzanel Mendiola
 * 
 */
public class ValidarUsuario extends AsyncTask<Object, Object, String> {

	private JSONObject login;
	private String dirURL;
	private boolean result;
	public ValidarUsuario(JSONObject login) {
		super();
		this.login = login;
		this.dirURL = "http://" + GpsManagerCostants.DIRECCION_IP
				+ ":8080/Servidor/webresources/persona/validar";
	}

	@Override
	protected void onPostExecute(String result) {
		Log.d("ValidarUsuario", result);
		
	}
	
	public boolean getResult(){
		return result;
	}
	
	@Override
	protected String doInBackground(Object... arg0) {

		String result;
		try {
			synchronized (this) {
				this.wait(10000);
			}

			if (postData(login)) {
				Log.i("EnviarCamino", "Se guarda");
			}

		} catch (Exception e) {
			result = e.toString();
		}
		return "Termino con exito";

	}

	public boolean postData(JSONObject object) {
		result = false;
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
