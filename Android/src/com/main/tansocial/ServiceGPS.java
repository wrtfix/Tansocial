package com.main.tansocial;

import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.widget.tansocial.Widget;

/**
 * 
 * Hereda de Service permitiendo que la aplicación permanezca en segundo plano e
 * implementa LocationListener que es la clase que provee el servicio de
 * localización. Con la inicialización del servicio se comienza a monitorear el
 * camino a través de la geolocalización del dispositivo móvil. Es capaz de
 * detectar todos los puntos geolocalizados del dispositivo móvil cada vez que
 * este cambia de posición, pero solo se tienen en cuenta, es decir se envían al
 * broadcast los puntos cuando tienen cierto grado de separación del punto
 * anterior. La información obtenida es enviada al Broadcast mediante un intent,
 * para que este luego la procese. A su vez se incorporó la creación de la
 * notificación con la utilización de un layout personalizado que indicara
 * cuando el servicio se inició, la misma permitirá detener el service y ver el
 * camino realizado con la actual ubicación del dispositivo. Se le incorporo un
 * BroadcastReceiver, controler, que permitirá detener el servicio a través de
 * la notificación o el widget.
 * 
 * @author Manzanel Mendiola
 */
public class ServiceGPS extends Service implements LocationListener {

	public static String START = "com.main.tansocial.START";

	protected LocationManager locationManager;
	private Notification noti1; 
	protected Location actual;
	protected GeoPoint lastGeoPoint;
	private Location posAnterior;

	private ArrayList<String> puntos = new ArrayList<String>();

	protected double meters;
	protected double speed;

	private float distancia = 0;
	private float tiempo = 0;
	private boolean detener;

	/**
	 * broadcastreciver que espera intents para detener el monitoreo
	 */
	private BroadcastReceiver controlador = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("STOP")) {
				Log.i("Controler", "Detener SERVICE GPS");
				detener = false;
				//Quitamos barra de notificaciones
				
				enviarDetenerNoti();
				//stopServiceGPS();
			}
			else if (intent.getAction().equals("BACTUAL")) {
				Log.i("Controler", "llego ACTUAL");
				//Quitamos barra de notificaciones
				cancelNotificacion();
				enviarActual();
			}
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	
	@Override
	public void onCreate() {
		super.onCreate();
		posAnterior = null;
		// registro de espera de STOP y ACTUAL
		IntentFilter filter2 = new IntentFilter("BACTUAL");
		registerReceiver(controlador, filter2);
		IntentFilter filter =  new IntentFilter("STOP");
		registerReceiver(controlador, filter);
		
		Log.i("ServiceGPS", "empezo el servicio");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		locationManager = (LocationManager) this.getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);

		// configuracion de las variables tiempo y distancia
		if (intent.getStringExtra("distancia") != null)
			this.distancia = Float.valueOf(intent.getStringExtra("distancia"));
		else
			distancia = 25;
		if (intent.getStringExtra("tiempo") != null)
			tiempo = Float.valueOf(intent.getStringExtra("tiempo"));
		else
			tiempo = 25;

		Log.i("ServiceGPS", "recibe un distancia: " + distancia);

		detener = true;
		resumePositionListener();
		super.onStart(intent, startId);

		crearNotificacion();
	}

	private void actualizarWidget(double lat, double lng) {
		Context context = this;
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget);
		ComponentName thisWidget = new ComponentName(context, Widget.class);
		remoteViews.setTextViewText(R.id.widgetMensaje, "Posicion Actual "
				+ lat + " , " + lng);
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);
	}

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(getApplicationContext(), "termino el servicio",
				Toast.LENGTH_SHORT).show();
		Log.i("ServiceGPS", "termino el servicio");
		this.stopForeground(true);
		pausePositionListener();
		unregisterReceiver(controlador);
	}

	public void pausePositionListener() {
		try {
			locationManager.removeUpdates((LocationListener) this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public void resumePositionListener() {
		try {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, (long) tiempo, distancia,
					(LocationListener) this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public void cancelNotificacion(){
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager;
		mNotificationManager = (NotificationManager) getSystemService(ns);
		mNotificationManager.cancel(1234);
		noti1.flags |= Notification.FLAG_AUTO_CANCEL;
		
		
	}
	/**
	 * metodo que verifica que no se tomen los mismos puntos geograficos no fue
	 * necesario, debido a que esta implementado por el locationlistener
	 * 
	 * @param actual
	 * @return
	 */
	public boolean distintaPosicion(Location actual) {
		if (posAnterior != null) {
			double lngactual = Math.abs(actual.getLongitude());
			double lnganterior = Math.abs(posAnterior.getLongitude());
			double latactual = Math.abs(actual.getLatitude());
			double latanterior = Math.abs(posAnterior.getLatitude());
			if (lngactual > lnganterior)
				if ((lngactual - lnganterior > distancia))
					return true;
				else if (latactual > latanterior)
					if ((latactual - latanterior > distancia))
						return true;
					else
						return false;
				else if ((latanterior - latactual > distancia))
					return true;
				else
					return false;
			else if ((lnganterior - lngactual > distancia))
				return true;
			else if (latactual > latanterior)
				if ((latactual - latanterior > distancia))
					return true;
				else
					return false;
			else if ((latanterior - latactual > distancia))
				return true;
			else
				return false;
		} else
			return true;

	}

	/**
	 * cada nueva posicion registrada por el GPS se procesa de la siguiente
	 * manera: mediante un intent comunicara al broadcast todos los datos
	 * necesarios
	 */
	public void onLocationChanged(Location location) {

		actual = location;

		// if (distintaPosicion(actual)){
		lastGeoPoint = new GeoPoint((int) (actual.getLatitude() * 1E6),
				(int) (actual.getLongitude() * 1E6));

		puntos.add(actual.getLatitude() + "#" + actual.getLongitude());
		actualizarWidget(actual.getLatitude(), actual.getLongitude());
		Intent i = new Intent("NuevaPosicion");
		i.putExtra("GPS", GpsManagerCostants.NEW_POSITION);
		i.putExtra(GpsManagerCostants.ACCURACY, location.getAccuracy());
		i.putExtra(GpsManagerCostants.ALTITUDE, location.getAltitude());
		i.putExtra(GpsManagerCostants.BEARING, location.getBearing());
		i.putExtra(GpsManagerCostants.EXTRAS, location.getExtras());
		i.putExtra(GpsManagerCostants.LATITUDE, location.getLatitude());
		i.putExtra(GpsManagerCostants.LONGITUDE, location.getLongitude());
		i.putExtra(GpsManagerCostants.SPEED, location.getSpeed());
		i.putExtra(GpsManagerCostants.TIME, location.getTime());

		Toast.makeText(getApplicationContext(),
				"punto nuevo, distancia: " + distancia, Toast.LENGTH_SHORT)
				.show();
		for (int j = 0; j < puntos.size(); j++) {
			Log.i("ServiceGPS", puntos.get(j));

		}
		if (detener == true)
			i.putExtra("Monitoreo", true);
		else
			i.putExtra("Monitoreo", false);

		i.putStringArrayListExtra("punto", this.puntos);

		LocalBroadcastManager.getInstance(this).sendBroadcast(i);
		Log.i("ServiceGPS", "cambia la posicion");
		posAnterior = location;
		// }

	}

	public void onProviderDisabled(String provider) {
		Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
		i.putExtra("GPS", GpsManagerCostants.SIGNAL_LOST);
		sendBroadcast(i);
	}

	/**
	 * Verifica el correcto funcionamiento del gps en caso de baja por algun
	 * motivo
	 */
	public void onStatusChanged(String provider, int status, Bundle extras) {
		if (provider.equals(LocationManager.GPS_PROVIDER)) {
			Intent i = null;
			switch (status) {
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
			case LocationProvider.OUT_OF_SERVICE:
				i = new Intent(Intent.ACTION_SEND_MULTIPLE);
				i.putExtra("GPS", GpsManagerCostants.SIGNAL_LOST);
				sendBroadcast(i);
				break;

			case LocationProvider.AVAILABLE:
				i = new Intent(Intent.ACTION_SEND_MULTIPLE);
				i.putExtra("GPS", GpsManagerCostants.SIGNAL_ENGAGED);
				sendBroadcast(i);
				break;
			}
		}
	}

	/**
	 * detiene el serviceGPS
	 */
	
	public void enviarDetenerNoti(){
		Log.i("ServiceGPS", "el service envia detenerNoti");
		Intent detener = new Intent("DetenerNoti");
		LocalBroadcastManager.getInstance(this).sendBroadcast(detener);
	}
	
	public void enviarActual(){
		Intent i = new Intent("Actual");
		LocalBroadcastManager.getInstance(this).sendBroadcast(i);
	}
	
	
	public void stopServiceGPS() {
		this.onDestroy();
	}

	public void onProviderEnabled(String arg0) {
		Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
		i.putExtra("GPS", GpsManagerCostants.SIGNAL_ENGAGED);
		sendBroadcast(i);
	}

	/**
	 * funcion que crea la notification mediante el layout se comunica mediante
	 * pendingintents con broadcast para mantener coherencia con el mapa
	 */
	@SuppressLint("NewApi")
	private void crearNotificacion() {
		Intent intent = new Intent("notificacion");
		PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

		// EJEMPLO con vistas
		RemoteViews notificationLayout = new RemoteViews(this.getPackageName(),
				R.layout.notification);

		// Declaracion de intents
		Intent intent3 = new Intent("BACTUAL");
		PendingIntent pendingIntent3 = PendingIntent.getBroadcast(this, 0,
				intent3, Notification.FLAG_AUTO_CANCEL);
		
		Intent intent4 = new Intent("STOP");
		PendingIntent pendingIntent5 = PendingIntent.getBroadcast(this, 0,
				intent4, PendingIntent.FLAG_UPDATE_CURRENT);
		
		notificationLayout.setOnClickPendingIntent(R.id.Actual, pendingIntent3);
		notificationLayout.setOnClickPendingIntent(R.id.Detener, pendingIntent5);
		
		
		noti1 = new Notification.Builder(this)
		.setContentTitle("TanSocial").setContentText("Monitoreando...")
		.setSmallIcon(R.drawable.ic_launcher)
		.setContent(notificationLayout).build()
		;
		

		
		this.startForeground(1234, noti1);

	}

}