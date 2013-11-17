package com.modelo.tansocial;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.main.tansocial.R;

/**
 * hereda de SQLiteOpenHelper y permitirá realizar la inserción, borrado,
 * actualización y consulta de los caminos monitoreados en un dispositivo.
 * 
 * @author Jorge Carlos Mendiola
 * 
 */

//FIXME Cambiar nombre a SQLLiteUtil luego de hacer merge con proyecto del pampeano.

public class GuardarCamino extends SQLiteOpenHelper {
	
	public GuardarCamino(Context contexto, String nombre,
			CursorFactory factory, int version) {
		super(contexto, nombre, factory, version);
	}

	// Sentencia SQL para crear la tabla de Usuarios
	String Camino = "CREATE TABLE Camino (id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, camino TEXT, horaInicio INTEGER, horaFin INTEGER, medioTransporte TEXT, precio TEXT, clima TEXT)";			
	String Lugar = "CREATE TABLE Lugar (id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, descripcion TEXT,punto TEXT, fecha INTEGER)";

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Se ejecuta la sentencia SQL de creación de la tabla
		db.execSQL("DROP TABLE IF EXISTS Camino");
		db.execSQL("DROP TABLE IF EXISTS Lugar");
		db.execSQL(Camino);
		db.execSQL(Lugar);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,
			int versionNueva) {
		// Se elimina la versión anterior de la tabla
		Log.i("GuardaCarmino", "Actualizando tabla....");
		db.execSQL("DROP TABLE IF EXISTS Camino");
		// Se crea la nueva versión de la tabla
		db.execSQL(Camino);
		Log.i("GuardaCarmino", "Se actualizo con EXITO");
		
	}

}
