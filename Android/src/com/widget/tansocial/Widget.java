package com.widget.tansocial;

import com.main.tansocial.Broadcast;
import com.main.tansocial.OpenMap;
import com.main.tansocial.R;
import com.main.tansocial.ServiceGPS;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.widget.RemoteViews;

/**
 * provee la funcionalidad de iniciar el ServiceGPS en caso de que el mismo no
 * esté inicializado utilizando el método PendingIntent.getService y también se
 * utilizó el método PendingIntent.getBroadcast el cual a través de un intent
 * enviará la solicitud de detener el ServiceGPS. A su vez permite ver en un
 * mapa el método PendingIntent.getActivity el cual permite inicializar un
 * Activity. El mismo es actualizado cada vez que el ServiceGPS reconoce una
 * nueva posición.
 * 
 * @author Orteguita
 * 
 */

public class Widget extends AppWidgetProvider {
	private final int INTENT_FLAGS = 0;
	private final int REQUEST_CODE = 0;

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		Log.i("Widget", "Crear el evento");
		RemoteViews widgetLayout = new RemoteViews(context.getPackageName(),
				R.layout.widget);

		Intent intent2 = new Intent(context, ServiceGPS.class);
		PendingIntent pendingIntent2 = PendingIntent.getService(context, 0,
				intent2, 0);

		Intent intent = new Intent("STOP");
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Intent intent3 = new Intent(context, OpenMap.class);
		intent3.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
		PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0,
				intent3, PendingIntent.FLAG_UPDATE_CURRENT);

		widgetLayout.setOnClickPendingIntent(R.id.monitorear, pendingIntent2);
		widgetLayout.setOnClickPendingIntent(R.id.Detener, pendingIntent);
		widgetLayout.setOnClickPendingIntent(R.id.Actual, pendingIntent3);

		appWidgetManager.updateAppWidget(appWidgetIds, widgetLayout);

	}

	public static void actualizarWidget(Context context,
			AppWidgetManager appWidgetManager, int widgetId) {

		// Obtenemos la lista de controles del widget actual
		RemoteViews controles = new RemoteViews(context.getPackageName(),
				R.layout.widget);

		// Asociamos los 'eventos' al widget
		Intent intent = new Intent();
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		controles.setOnClickPendingIntent(R.id.Detener, pendingIntent);
		controles.setTextViewText(R.id.widgetMensaje, "Detener");
		appWidgetManager.updateAppWidget(widgetId, controles);
	}

}
