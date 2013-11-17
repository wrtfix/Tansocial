package com.main.tansocial;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.modelo.tansocial.GuardarCamino;
import com.service.tansocial.ObtenerInformacion;
import com.widget.tansocial.Widget;

/**
 * Presenta la vista del mapa. Consulta si se encuentra en funcionamiento el
 * ServiceGPS y en caso de que lo este se pintaran los puntos que fueron
 * obtenido con anterioridad por el service. Para el manejo visual del mapa, la
 * descarga y poseer la posibilidad de pintar un camino se incorporaron la
 * librería Osmdroid la cual permite utilizar lo mapas pertenecientes a
 * OpenStreetMap.
 * 
 * @author Manzanel, Mendiola
 * 
 * 
 */
public class OpenMap extends Activity {

	private MapView myOpenMapView;
	private MapController myMapController;
	private Double lat;
	private Double lng;
	private boolean primeraPosicion;
	private Broadcast receiver;
	private Button monitorear = null;
	private Button detener = null;
	private Button posicion = null;
	private String distancia = "25";
	private String tiempo = "1000";


	public void setIp(String ip) {
		if(!"".equals(ip))
			GpsManagerCostants.DIRECCION_IP = ip;
	}

	private int widgetId = 0;

	@Override
	/**
	 * Activity donde se lleva a cabo el mapa, 
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.openmap);

		myOpenMapView = (MapView) findViewById(R.id.openmapview);
		myOpenMapView.setBuiltInZoomControls(true);
		myOpenMapView.setMultiTouchControls(true);
		myMapController = myOpenMapView.getController();
		myMapController.setZoom(4);
		
		// inicio el broadcast
		receiver = new Broadcast(OpenMap.this);
		LocalBroadcastManager.getInstance(OpenMap.this).registerReceiver(
						receiver, new IntentFilter("NuevaPosicion"));
		LocalBroadcastManager.getInstance(OpenMap.this).registerReceiver(
						receiver, new IntentFilter("DetenerNoti"));
		LocalBroadcastManager.getInstance(OpenMap.this).registerReceiver(
						receiver, new IntentFilter("STOPBroad"));
		LocalBroadcastManager.getInstance(OpenMap.this).registerReceiver(
						receiver, new IntentFilter("Actual"));
		LocalBroadcastManager.getInstance(OpenMap.this).registerReceiver(
						receiver, new IntentFilter("ENVIARBroad"));
		LocalBroadcastManager.getInstance(OpenMap.this).registerReceiver(
						receiver, new IntentFilter("LUGARBroad"));
		
		// Al abrir el mapa este se pinta tal cual estaba en ultima instancia
		if (savedInstanceState != null) {
			lat = savedInstanceState.getDouble("LATITUD");
			lng = savedInstanceState.getDouble("LONGITUD");
			if (lat != null) {
				pintarMapa(lat, lng);
			}

		}
		// Definicion de botones
		monitorear = (Button) findViewById(R.id.monitorear);
		detener = (Button) findViewById(R.id.Detener);
		posicion = (Button) findViewById(R.id.posicion);

		// Consulto si esta corriendo el service para pintar el camino
		if (isServiceGPSRunning()) {
			primeraPosicion = true;
			setBotonMonitoreo(false);
		}

		posicion.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				miPosicion();
			}
		});

		// Detiene el service
		detener.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialogoGuardar();

			}
		});

		// Inicia el serviceGPS y el broadcast
		monitorear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				monitorear();
			}
		});
	}

	/**
	 * metodo que lleva a cabo el monitoreo
	 */
	public void monitorear() {

		// Inicio el servicio de GPS
		Intent intent = new Intent(OpenMap.this, ServiceGPS.class);
		intent.putExtra("distancia", distancia);
		intent.putExtra("tiempo", tiempo);
		startService(intent);

		
		// Devolvemos como resultado: ACEPTAR (RESULT_OK)
		Intent resultado = new Intent();
		resultado.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		setResult(RESULT_OK, resultado);

		
		// Actualizamos el widget tras la configuración
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(OpenMap.this);
		Widget.actualizarWidget(OpenMap.this, appWidgetManager, widgetId);

		primeraPosicion = true;
		setBotonMonitoreo(true);
	}

	/**
	 * Metodo para determinar si un servicio esta corriendo
	 * 
	 * @return
	 */
	private boolean isServiceGPSRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (ServiceGPS.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Pinta la ultima posicion que se escucho
	 * 
	 * @param view
	 */

	public void miPosicion() {
		try {
			if (this.lat != null) {
				primeraPosicion = true;
				agregarAtributo();
				pintarMapa(this.lat, this.lng);
			} else {
				mensajeError("Mi posicion");
			}
		} catch (Exception e) {
			Log.e("OpenMap", e.toString());
		}
	}

	/**
	 * Esta funcion abre un dialogo en el cual solicita nombre de atributo y valor 
	 */
	 
	private void agregarAtributo() {
		
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.atributo);
		dialog.setTitle("Agregar Lugar");

		Button aceptar = (Button) dialog.findViewById(R.id.agregarAtributo);
		Button cancelar = (Button) dialog.findViewById(R.id.cancelarAtributo);

		aceptar.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				
				
				TextView nombre = (TextView) dialog.findViewById(R.id.nombreAtributo);
				TextView valor = (TextView) dialog.findViewById(R.id.valorAtributo);
				
				Intent i = new Intent("LUGARBroad");
				String name = nombre.getText().toString();
				String descrpcion = valor.getText().toString();
				
				i.putExtra("LUGARBroadNombre", name);
				i.putExtra("LUGARBroadDescrpcion", descrpcion);
				
				LocalBroadcastManager.getInstance(OpenMap.this)
						.sendBroadcast(i);

				dialog.dismiss();
			}
		});

		cancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		dialog.show();
	}

	private void detener() {

		Intent i = new Intent("STOPBroad");
		i.putExtra("Monitoreo", false);
		LocalBroadcastManager.getInstance(OpenMap.this).sendBroadcast(i);

		
		Intent intent = new Intent(OpenMap.this, ServiceGPS.class);
		stopService(intent);

	}

	/**
	 * lleva a cabo las acciones una vez precionado el boton detener, este
	 * muestra las opciones al usuario de guardar camino mediante un
	 * AlertDialog.
	 */
	public void dialogoGuardar() {
		if (this.lat != null) {
			
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.guardarcamin);
			dialog.setTitle("Guardar Camino");

			Button aceptar = (Button) dialog.findViewById(R.id.guardarAceptar);
			Button cancelar = (Button) dialog.findViewById(R.id.guardarCancelar);

			aceptar.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					
					//FIXME Verificar si esta sentencia esta bien colocada en este lugar :S
					LocalBroadcastManager.getInstance(OpenMap.this).registerReceiver(receiver, new IntentFilter("GUARDARBroad"));
					
					TextView nombre = (TextView) dialog
							.findViewById(R.id.guardarNombre);
					TextView medioTransporte = (TextView) dialog
							.findViewById(R.id.guardarTransporte);
					TextView precio = (TextView) dialog
							.findViewById(R.id.guardarPrecio);
					TextView clima = (TextView) dialog
							.findViewById(R.id.guardarClima);
					
					Toast.makeText(
							getApplicationContext(),
							"Se guardo el camino con el nombre de:"
									+ nombre.getText(),
							Toast.LENGTH_SHORT).show();
					
					
					Intent i = new Intent("GUARDARBroad");
					
					String name = nombre.getText().toString();
					String transporte = medioTransporte.getText().toString();
					String costo= precio.getText().toString();
					String climate = clima.getText().toString();
					
					i.putExtra("GUARDARBroadNombre", name);
					i.putExtra("GUARDARBroadTransporte", transporte);
					i.putExtra("GUARDARBroadCosto", costo);
					i.putExtra("GUARDARBroadClima", climate);

					LocalBroadcastManager.getInstance(OpenMap.this).sendBroadcast(i);
					primeraPosicion = true;
					
					dialog.dismiss();
					detener();

				}
			});

			cancelar.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			
			dialog.show();

			
		} else {
			detener();
			setBotonMonitoreo(false);
			mensajeError("Guardar Camino");
		}

	}

	@Override
	/**
	 * Presenta el menu de android con las opciones de:
	 * wikipedia, enviar, guardar, configuracion.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.wikipedia: {
			if (this.lat != null) {
				Toast.makeText(getApplicationContext(),
						"Obteniendo informacion de su posicion",
						Toast.LENGTH_SHORT).show();
				this.mostrarInformacion(lat, lng);
			} else {
				mensajeError("Wikipedia");
			}

			return true;
		}
		case R.id.Enviar: {
			enviarElemento("Camino","GUARDARBroad");
			return true;
		}

		case R.id.EnviarLugar: {
			enviarElemento("Lugar","ENVIARLugarBraod");
			return true;
		}

		case R.id.Guardar: {
			this.dialogoGuardar();
			return true;
		}
		
		case R.id.Configuracion: {
			// custom dialog
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.configuracion);
			dialog.setTitle("Configuracion");

			Button aceptar = (Button) dialog.findViewById(R.id.Aceptar);
			Button cancelar = (Button) dialog.findViewById(R.id.Cancelar);

			aceptar.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					TextView ipt = (TextView) dialog.findViewById(R.id.Ip);
					TextView distanciat = (TextView) dialog
							.findViewById(R.id.editDistancia);
					TextView tiempot = (TextView) dialog
							.findViewById(R.id.EditTiempo);
					if (!"".equals(ipt.getText().toString()))
						setIp(ipt.getText().toString());
					if (!"".equals(distanciat.getText().toString()))
						setDistancia(distanciat.getText().toString());
					if (!"".equals(tiempot.getText().toString()))
						setTiempo(tiempot.getText().toString());

					dialog.dismiss();

				}
			});

			cancelar.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();

			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Este metodo obtiene los lugares y camino guardados en la base de adtos
	 * @param nombre
	 * @return
	 */
	private List<String> getDatabase(String nombre){
		GuardarCamino gc = new GuardarCamino(this, "CAM", null, 1);
		SQLiteDatabase db = gc.getWritableDatabase();
		final List<String> listItems = new ArrayList<String>();
		if (db != null) {
			Cursor c = db.rawQuery("SELECT nombre FROM "+nombre, null);
			if (c.moveToFirst()) {
				do {
					listItems.add(c.getString(0));
				} while (c.moveToNext());
			}

			db.close();
			if (c.getCount() == 0) {
				mensajeError("No se encontraron resultados");
			}

		}
		return listItems;
		
	}
	
	
	
	/**
	 * Envia elementos al servidor
	 * @param tabla
	 * @param nombreIntent
	 * @return
	 */
	private boolean enviarElemento(String tabla, final String nombreIntent) {
		
		final List<String> listItems = getDatabase(tabla);
		final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
		final ArrayList mSelectedItems = new ArrayList();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Seleccione "+tabla+" a enviar")
				.setMultiChoiceItems(items, null,
						new DialogInterface.OnMultiChoiceClickListener() {
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								if (isChecked) {
									mSelectedItems.add(which);
								} else if (mSelectedItems.contains(which)) {
									mSelectedItems.remove(Integer
											.valueOf(which));
								}
							}
						})
				.setPositiveButton("Enviar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								String nombre = null;
								for (int i = 0; i < mSelectedItems.size(); i++) {
									nombre = listItems.get((Integer) mSelectedItems.get(i));
									Log.i("OpenMap", nombre);

								}
								if (receiver == null)
									receiver = new Broadcast(OpenMap.this);

								LocalBroadcastManager.getInstance(OpenMap.this).registerReceiver(receiver,new IntentFilter(nombreIntent));
								Intent intent = new Intent(nombreIntent);
								intent.putExtra("nombre", nombre);
								LocalBroadcastManager.getInstance(OpenMap.this).sendBroadcast(intent);

							}
						})
				.setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {

							}
						});

		builder.show();
		return true;
		
	}
	
	
	/**
	 * Si el monitoreo no fue exitoso presenta un AlertDialog con el mensaje de
	 * error
	 * 
	 * @param titulo
	 */
	private void mensajeError(String titulo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(titulo);
		builder.setMessage("Tiene que haber monitoreado por lo menos una vez");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.openmap, menu);
		return true;
	}

	/**
	 * Dibuja la posicion en el mapa
	 * 
	 * @param lat
	 * @param lng
	 */
	public void pintarMapa(Double lat, Double lng) {
		this.lat = lat;
		this.lng = lng;

		Log.i("openMap", "pinta");
		ArrayList<OverlayItem> anotherOverlayItemArray = new ArrayList<OverlayItem>();
		GeoPoint punto = new GeoPoint(lat, lng);
		anotherOverlayItemArray.add(new OverlayItem("Tandil", "Tandil", punto));
		Object myOnItemGestureListener = null;
		ItemizedOverlayWithFocus<OverlayItem> anotherItemizedIconOverlay = new ItemizedOverlayWithFocus<OverlayItem>(
				this, anotherOverlayItemArray,
				(OnItemGestureListener<OverlayItem>) myOnItemGestureListener);
		myOpenMapView.getOverlays().add(anotherItemizedIconOverlay);
		anotherItemizedIconOverlay.setFocusItemsOnTap(true);
		if (primeraPosicion) {
			myMapController.animateTo(punto);
			primeraPosicion = false;
		}
		myOpenMapView.refreshDrawableState();
		
	}

	/**
	 * Obtencion asincrona a wikipedia con JSON
	 * 
	 * @param lat
	 * @param lng
	 */
	public void mostrarInformacion(Double lat, Double lng) {
		Log.d("ATASK", "Creating asynctask  " + lat + "," + lng);
		ObtenerInformacion at = new ObtenerInformacion(lat, lng, this);
		at.execute();

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		if (this.lat != null) {
			savedInstanceState.putDouble("LATITUD", this.lat);
			savedInstanceState.putDouble("LONGITUD", this.lng);
			super.onSaveInstanceState(savedInstanceState);
		}
	}

	/**
	 * setea los botones de acuerdo a las acciones del usuario
	 * 
	 * @param estado
	 */
	public void setBotonMonitoreo(boolean estado) {
		if (monitorear != null) {
			if (estado) {
				detener.setEnabled(true);
				monitorear.setEnabled(false);
			} else {
				monitorear.setEnabled(true);
				detener.setEnabled(false);
			}
		}
	}

	public String getDistancia() {
		return distancia;
	}

	public void setDistancia(String marcas) {
		this.distancia = marcas;
	}

	public String getTiempo() {
		return tiempo;
	}

	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}

}
