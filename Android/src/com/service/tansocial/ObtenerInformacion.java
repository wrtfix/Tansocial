package com.service.tansocial;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.main.tansocial.R;

/**
 * hereda de AsyncTask lo cual nos permitió obtener información de manera
 * asíncrona de la posición donde se encuentre el dispositivo así como también
 * enviar los caminos generados por el dispositivo. Se logro consultando a un
 * Servidor llamado nominatim.openstreetmap.org el cual produce respuestas del
 * tipo JSON y su implementación no provoco ningún inconveniente ya que los
 * dispositivos Android proveen de manera nativa los mecanismos necesarios para
 * manipular la información.
 * 
 * @author Manzanel Mendiola
 * 
 */

public class ObtenerInformacion extends AsyncTask<Object, Object, String> {
	private Double lat;
	private Double lng;
	private String direccion;
	private Context c;

	public ObtenerInformacion(Double lat, Double lgn, Context c) {
		super();
		this.lat = lat;
		this.lng = lgn;
		this.c = c;
	}

	@Override
	protected void onPostExecute(String result) {
		String readTwitterFeed = result;
		try {
			JSONObject object = (JSONObject) new JSONTokener(readTwitterFeed)
					.nextValue();
			direccion = object.getString("display_name");
			Log.i("Obtener informacion", direccion);
			Toast.makeText(c, direccion, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getDireccion() {
		return direccion;
	}

	@Override
	protected String doInBackground(Object... arg0) {

		Log.d("ATASK", "Executing in background "
				+ Thread.currentThread().getName());
		String result;
		try {
			synchronized (this) {
				this.wait(10000);
			}
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(
					"http://nominatim.openstreetmap.org/reverse?format=json&lat="
							+ lat + "&lon=" + lng + "&zoom=18&addressdetails=1"));
			HttpResponse response = client.execute(request);
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer();
			String line = bf.readLine();
			while (line != null) {
				sb.append(line);
				line = bf.readLine();
			}
			Log.i("ObtenerInformacion", sb.toString());
			result = sb.toString();
		} catch (Exception e) {
			result = e.toString();
		}
		Log.d("ATASK", "Finishing " + Thread.currentThread().getName());
		return result;

	}

}
