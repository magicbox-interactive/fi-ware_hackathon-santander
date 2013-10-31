package es.magicbox.hackathon.widgets.async;

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import es.magicbox.hackathon.widgets.CommonApp;
import es.magicbox.hackathon.widgets.WidgetsPortalActivity;
import es.magicbox.hackathon.widgets.adapter.HScrollAdapter;
import es.magicbox.hackathon.widgets.gestion.GestorWidgets;
import es.magicbox.hackathon.widgets.modelo.Widget;

/**
 * tarea asincrona para obtener los widgets
 * 
 */
public class WidgetAsync extends AsyncTask<Void, Void, List<Widget>> {

	/**
	 * Numero de elementos que muestra el scroll horizontal
	 */
	private static final int ELEMENTOS = 4;
	/**
	 * TAG de logs
	 */
	private static final String TAG = WidgetAsync.class.getSimpleName();
	/**
	 * Contexto de la activity
	 */
	private Context contexto;
	/**
	 * Objeto comun de la aplicacion
	 */
	private CommonApp commonApp;
	/**
	 * Objeto bundle necesario
	 */
	private Bundle bundle;

	/**
	 * Constructor de la tarea asincrona
	 * 
	 * @param context
	 *            Contexto
	 */
	
	private List<Widget> fillWidgetList()
	{
		List<Widget> widgetList = new ArrayList<Widget>();

//		Widget _newWidget1 = new Widget();
//		_newWidget1.setNombre("Widget1");
//		widgetList.add(_newWidget1);
//
//		Widget _newWidget2 = new Widget();
//		_newWidget2.setNombre("Widget2");
//		widgetList.add(_newWidget2);
//
//		Widget _newWidget3 = new Widget();
//		_newWidget3.setNombre("Widget3");
//		widgetList.add(_newWidget3);

		return widgetList;
	}
	
	
	/**
	 * 4ltrv1sm0
	 * @param context
	 * @param bundle
	 */
	
	public WidgetAsync(Context context, Bundle bundle) {
		Log.v(TAG, "INICIO  WidgetAsync");
		this.contexto = context;
		this.bundle = bundle;
		this.commonApp = ((CommonApp) ((Activity) context).getApplication());
		Log.v(TAG, "FIN  WidgetAsync");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected List<Widget> doInBackground(Void... params) {
		Log.v(TAG, "INICIO doInBackground de WidgetAsync");
		GestorWidgets gw = new GestorWidgets(contexto);
		List<Widget> widgetList = null;

		//descargamos o cargamos de local los widgets
		try {
//			if (Utils.isNetworkAvailable(contexto)) {
//				widgetList = gw.descargaXMLremotoWidgets(commonApp
//						.getResourceURL(CommonApp.PATH_COMUN)
//						+ CommonApp.PATH_WIDGETS_REMOTO);
//				Log.d(TAG, " ## Tamanio de los widget " + widgetList.size());
//
//				if (widgetList != null) {
//					gw.guardaXMLwidgets(
//							commonApp.getResourceURL(CommonApp.PATH_COMUN)
//									+ CommonApp.PATH_WIDGETS_REMOTO, widgetList);
//				}
//			} 
//			else 
			{
				//widgetList = gw.getContenidoWidgets(2,
				//		CommonApp.PATH_WIDGETS_LOCAL);
				
				// 4ltrv1sm0:
				widgetList = fillWidgetList();

			}
//
//		} catch (WidgetException e) {
//			Log.e(TAG, "WidgetException ", e);
//
		} catch (Exception e) {
			Log.e(TAG, "Error descargando contenido en WidgetAsync", e);
		}
		Log.v(TAG, "FIN doInBackground de WidgetAsync");
		return widgetList;
	}

	@Override
	protected void onPostExecute(List<Widget> result) {
		if (null != result) {
			HScrollAdapter adapter = new HScrollAdapter(this.contexto, result,
					this.bundle);
			adapter.adaptaContenidoBarraInflate();

			
			// Si mas de 4 alementos pongo flechas
//			if (result.size() > ELEMENTOS) {
//				// setArrowVisibles();
//				((ImageView) ((WidgetsPortalActivity) this.contexto)
//						.findViewById(R.id.flecha_izq))
//						.setVisibility(View.VISIBLE);
//				((ImageView) ((WidgetsPortalActivity) this.contexto)
//						.findViewById(R.id.flecha_dch))
//						.setVisibility(View.VISIBLE);
//			}
		
		}
		super.onPostExecute(result);
	}

}
