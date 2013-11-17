package com.main.tansocial;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.service.tansocial.EnviarUsuario;
/**
 * Esta clase permite registra nuevos usuarios en el servidor
 * @author Mendiola
 *
 */
public class RegistraUsuario extends Activity{
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);
        
        
        final TextView nombre = (TextView) this.findViewById(R.id.registrarnombre);
        final TextView apellido = (TextView) this.findViewById(R.id.registrarapellido);
        final TextView edad = (TextView) this.findViewById(R.id.registraredad);
        final TextView sexo = (TextView) this.findViewById(R.id.registrarsexo);
        final TextView ocupacion = (TextView) this.findViewById(R.id.registrarocupacion);
        final TextView email = (TextView) this.findViewById(R.id.registrarmail);
        final TextView telefono = (TextView) this.findViewById(R.id.registrartelefono);
        final TextView sueldo = (TextView) this.findViewById(R.id.registrarsueldo);
        final TextView contrasenia = (TextView) this.findViewById(R.id.registrarcontrasenia);
        final TextView nivelEstudio = (TextView) this.findViewById(R.id.registrarestudio);
        final TextView domicilio = (TextView) this.findViewById(R.id.registrardomicilio);
        
        final JSONObject usuario = new JSONObject();
    	try {
    		usuario.put("mail",email.getText());
			usuario.put("nroTelefono",telefono.getText());
			usuario.put("apellido",apellido.getText());
			usuario.put("nombre", nombre.getText());
			usuario.put("edad",edad.getText());
			usuario.put("sexo",sexo.getText());
			usuario.put("principalOcupacion",ocupacion.getText());
			usuario.put("sueldo", sueldo.getText());
			usuario.put("contrasenia",contrasenia.getText());
			usuario.put("nivelEstudio",nivelEstudio.getText());
			usuario.put("domicilio", domicilio.getText());
    	} catch (JSONException e) {
    		Log.e("RegistrarUsuario",e.toString());
		}
    	
        
        Button b1 =  (Button) findViewById(R.id.registraraceptar);
        b1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				agregarUsuario(usuario, v);
				Toast.makeText(v.getContext(), "Aguarde un momento", Toast.LENGTH_LONG).show();
				finish();
			}
		});
    }
	
	public void agregarUsuario(JSONObject usuario, View v) {
		Log.i("RegistrarUsuario", "Enviando datos del usuario");
		EnviarUsuario at = new EnviarUsuario(usuario, v);
		at.execute();
	}
	    


}