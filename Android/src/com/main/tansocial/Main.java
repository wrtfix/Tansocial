package com.main.tansocial;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Main extends Activity {
	private Intent i;
	private String dirURL = "http://" + GpsManagerCostants.DIRECCION_IP
			+ ":8080/Servidor/webresources/persona/validar";
	
	private boolean resultado= false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
		
    	final TextView nombre= (TextView) this.findViewById(R.id.textUsuario);
		final TextView contrasenia = (TextView) this.findViewById(R.id.textContra);
        
        Button b1 =  (Button) findViewById(R.id.login);
        b1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				JSONObject login = new JSONObject();
				try {
					login.put("nombre", nombre.getText());
					login.put("contrasenia",contrasenia.getText());
					ValidarUsuario at = new ValidarUsuario(login,v);
					at.execute();
					
					
				} catch (JSONException e) {
					Log.e("Main", e.toString());
				}
				
			}
		});
        Button b2 =  (Button) findViewById(R.id.registrarse);
        b2.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				registrarse(v);
			}
		});
    }

   public void ejecutar(View view) {
	   i = new Intent(this, OpenMap.class );
       i.putExtra("direccion", "probando");
       startActivity(i);
   }
   
   public void registrarse(View view) {
	   i = new Intent(this,  RegistraUsuario.class );
	   i.putExtra("direccion", "probando");
	   startActivity(i);
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
    /**
     * Algoritmo de seguridad  
     * @param pass
     * @return
     */
    private String toMd5(String pass){
        try{
            //Creando Hash MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(pass.getBytes());
            byte messageDigest[] = digest.digest();
 
            //Creando Hex String
            StringBuffer hexString = new StringBuffer();
            for(int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            Log.w("Pass en MD5: ", hexString.toString());
            return hexString.toString();
        }catch(NoSuchAlgorithmException ex){
            Log.w("NoSuchAlgorithmException", ex.toString());
            return null;
        }
    }
    
    
    
    public class ValidarUsuario extends AsyncTask<Object, Object, String> {

    	private JSONObject login;
    	private String dirURL;
    	private View v;
    	public ValidarUsuario(JSONObject login, View v) {
    		super();
    		this.login = login;
    		this.dirURL = "http://" + GpsManagerCostants.DIRECCION_IP
    				+ ":8080/Servidor/webresources/persona/validar";
    		this.v=v;
    	}

    	@Override
    	protected void onPostExecute(String result) {
    		Log.d("ValidarUsuario", result);
    		if(result !=null && "true".equals(result))
    			ejecutar(v);
    		else
    			Toast.makeText(v.getContext(), "Ocurrio un error con el servidor", Toast.LENGTH_LONG).show();
    			
    	}

    	@Override
    	protected String doInBackground(Object... arg0) {

    		try {
    			synchronized (this) {
    				this.wait(10000);
    			}
    			
    			
    			if (postData(login)) {
    				return  "true";
    				
    			}

    		} catch (Exception e) {
    			Log.e("ValidarUsuario",e.toString());
    			return "false";
    		}
    		return "false";

    	}

    	public boolean postData(JSONObject object) {
    		HttpClient hc = new DefaultHttpClient();
    		String message;

    		HttpPost p = new HttpPost(dirURL);

    		try {
    			message = object.toString();

    			p.setEntity(new StringEntity(message));
    			p.setHeader("Content-type", "application/json");
    			
    			HttpResponse resp = hc.execute(p);
    			if (resp != null) {
    				if (resp.getStatusLine().getStatusCode() == 200)
    					return true;
    			}

    			Log.d("ValidarUsuario", "" + resp.getStatusLine().getStatusCode());
    		} catch (Exception e) {
    			Log.e("ValidarUsuario",e.toString());

    		}
    		
    		Log.i("Main","Salio posdata");
    		
    		return false;
    	}

    }

    
    
    
    
}
