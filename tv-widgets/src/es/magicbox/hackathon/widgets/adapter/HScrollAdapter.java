package es.magicbox.hackathon.widgets.adapter;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import es.magicbox.hackathon.widgets.R;

import es.magicbox.hackathon.widgets.CommonApp;
import es.magicbox.hackathon.widgets.WidgetSecurity;
import es.magicbox.hackathon.widgets.WidgetsPortalActivity;
import es.magicbox.hackathon.widgets.exception.WidgetException;
import es.magicbox.hackathon.widgets.modelo.Widget;

/**
 * Crea los Views para cada widgets y las añade al HorizontalScrollview
 * 
 */
public class HScrollAdapter {

	/**
	 * Tag para los logs
	 */
	private static final String TAG = HScrollAdapter.class.getSimpleName();
	/**
	 * Contexto de la actividad
	 */
	private final Context contexto;
	/**
	 * Objeto comun
	 */
	private CommonApp commonApp;
	/**
	 * Listado de widgets
	 */
	private List<Widget> widgetList;
	/**
	 * "Inflador" de la fila
	 */
	private LayoutInflater li;

	/**
	 * 
	 */
	private Bundle bundle;

	/**
	 * Constructor del adaptador
	 * 
	 * @param context
	 *            Contexto
	 * @param widgetList
	 *            Listado de Widgets
	 */
	public HScrollAdapter(Context context, List<Widget> widgetList,
			Bundle bundle) {
		this.contexto = context;
		this.widgetList = widgetList;
		this.bundle = bundle;
		// Recuperamos el objeto comun
		this.commonApp = ((CommonApp) ((Activity) context).getApplication());
	}

	/**
	 * Adaptador del contenido al HorizontalScroll
	 */
	public void adaptaContenidoBarraInflate() {
		Log.v(TAG, "INICIO adaptaContenidoBarraInflate ");
		try {
			// Preparando las rutas de iconos para enviarselas al AsyncTask
			String[] urlIconos = null;
//			int indice = 0;
//			if (!widgetList.isEmpty()) {
//
//				urlIconos = new String[widgetList.size() * 2];
//
//				for (Widget icon : widgetList) {
//					urlIconos[indice] = icon.getIconValues().get(0);
//					urlIconos[indice + 1] = icon.getIconValues().get(1);
//					indice += 2;
//				}
//			}
//
//			for (String cad : urlIconos) {
//				Log.d(TAG, "# " + cad);
//
//			}

			// Ejecutando el IconWidgetAsync
			new IconWidgetAsync(this.contexto).execute(urlIconos);

		} catch (Exception e) {
			Log.e(TAG, "Error en el adapter", e);
		}
		Log.v(TAG, "FIN adaptaContenidoHorizontalInflate ");
	}

	private IconoWidget getIconByResourceID(int ResourceID, String resourceName)
	{
		IconoWidget _icon = null;
		Bitmap _bitmap;

		_bitmap = BitmapFactory.decodeResource(this.contexto.getResources(), ResourceID)  ;
		if (_bitmap != null)
		{
			_icon = new IconoWidget();
			_icon.setBitmap(_bitmap);
			_icon.setNombre(resourceName);
			
		}
		
		return _icon;
	}
	
	
	/**
	 * @author
	 * 
	 */
	private class IconWidgetAsync extends
			AsyncTask<String, Void, List<IconoWidget>> {
		private Context contexto;

		public IconWidgetAsync(Context contexto) {
			this.contexto = contexto;
		}

		@Override
		protected List<IconoWidget> doInBackground(String... params) {

//			List<Bitmap> bitmaps = null;
			List<IconoWidget> bitmapDescargados = new ArrayList<IconoWidget>();
						
			// 4ltrv1sm0:
			bitmapDescargados.add(getIconByResourceID(R.drawable.ico_sensores, "Widget1"));
			bitmapDescargados.add(getIconByResourceID(R.drawable.ico_sensores, "Widget1"));
			bitmapDescargados.add(getIconByResourceID(R.drawable.ico_presencia, "Widget2"));
			bitmapDescargados.add(getIconByResourceID(R.drawable.ico_presencia, "Widget2"));

//			
//			URL url = null;
//			URLConnection conn = null;
//			Bitmap bitmap = null;
//
//			String path_comun = commonApp.getResourceURL(CommonApp.PATH_COMUN);
//			if (params != null && params.length > 0) {
//				bitmaps = new ArrayList<Bitmap>();
//				for (int i = 0; i < params.length; i++) {
//
//					try {
//						url = new URL(path_comun + params[i]);
//
//						if (url.getProtocol().equalsIgnoreCase("https")) {
//							conn = url.openConnection();
//							((HttpsURLConnection) conn)
//									.setHostnameVerifier(Utils.DO_NOT_VERIFY);
//						} else {
//							conn = url.openConnection();
//						}
//						conn.setDoInput(true);
//						conn.setReadTimeout(Utils.CONNECTION_TIMEOUT_SECONDS);
//						conn.connect();
//						InputStream is = conn.getInputStream();
//
//						bitmap = BitmapFactory.decodeStream(is);
//						bitmaps.add(bitmap);
//					} catch (MalformedURLException e) {
//						bitmaps.add(null);
//						Log.e(TAG, e.getMessage());
//					} catch (IOException e) {
//						bitmaps.add(null);
//						Log.e(TAG, e.getMessage());
//					} catch (Exception e) {
//						bitmaps.add(null);
//						Log.e(TAG, e.getMessage());
//					}
//				}
//			}
//
//			Log.d(TAG, "Numero bitmaps :  " + bitmaps.size());
//			int i = 0;
//			if (bitmaps != null && !bitmaps.isEmpty()) {
//				for (Bitmap bb : bitmaps) {
//					// nos aseguramos de no añadir imagenes a la lista que no se
//					// hayan podido descargar.
//					if (bb == null) {
//						if (i % 2 == 0) {
//							// si es nulo en la posicion PAR eliminar la
//							// posicion
//							// impar tambien la ponemos a null
//							bitmaps.set(i, null);
//							bitmaps.set(i + 1, null);
//
//						} else {
//							// si es nulo en la posicion impar eliminar la
//							// anterior
//							// posicion par. tambien la ponemos a null
//							bitmaps.set(i, null);
//							bitmaps.set(i - 1, null);
//
//						}
//					}
//					i++;
//				}
//
//				int pos = 0;
//				if (bitmaps != null && !bitmaps.isEmpty()) {
//
//					for (Bitmap nuevo : bitmaps) {
//						// Solo agregamos a la lista los bitmap que sean
//						// diferente
//						// de NULL
//						if (nuevo != null) {
//							IconoWidget iconoWidget = new IconoWidget();
//							iconoWidget.setBitmap(nuevo);
//							iconoWidget.setNombre(params[pos]);
//							bitmapDescargados.add(iconoWidget);
//						}
//						pos++;
//
//					}
//				}
//
//			}
//
//			Log.d(TAG,
//					"Numero de imagenes descargadas :  "
//							+ bitmapDescargados.size());

			return bitmapDescargados;

		}

		@Override
		protected void onPostExecute(List<IconoWidget> result) {
			try {

				if (result == null || result.isEmpty())
					throw new WidgetException(
							"## La lista de IconoWidget esta vacia o nula");

				// para pintar los iconos se crearan en par de imagenes, por
				// ejemplo la posicion 0 y 1 del listado "result"
				// conformaran el
				// primer icono icono sin tener el foco y el otro teniendo
				// el
				// foco.
				// Obtengo el layout del scrollview horizontal
				LinearLayout layoutScroll = (LinearLayout) ((Activity) contexto)
						.findViewById(R.id.hcontenido);
				layoutScroll.setFocusable(false);
				layoutScroll.removeAllViews();

				int tamanio = result.size() / 2;
				int pos = 0;
				// variables para recuperar los iconos
				// final Bitmap bitmapOn = null;
				// final Bitmap bitmapOff = null;

				for (int i = 0; i < tamanio; i++) {

					String valorIcono = "";

					if (result.get(pos).getNombre().equals(result.get(pos + 1).getNombre())) {
						valorIcono = result.get(pos).getNombre();
					} else {
						valorIcono = result.get(pos).getNombre() + "|" + result.get(pos + 1).getNombre();
					}

					final String nombreIcono = valorIcono;

					final Bitmap bitmapOn = result.get(pos).getBitmap();
					final Bitmap bitmapOff = result.get(pos + 1).getBitmap();

					// Inflo items
					li = LayoutInflater.from(contexto);
					View itemInflate = li
							.inflate(R.layout.iconocontenido, null);

					// Obtenemos la linea de separacion entre items
					ImageView lineaItemInflado = (ImageView) itemInflate
							.findViewById(R.id.lineaIcon);

					final LinearLayout imagenLayout = (LinearLayout) itemInflate
							.findViewById(R.id.imagenLayout);
					imagenLayout.setFocusable(true);

					final ImageView imagenOff = (ImageView) itemInflate
							.findViewById(R.id.imgIcon);
					imagenOff.setImageBitmap(bitmapOff);
					imagenOff.setFocusable(true);
					imagenOff.setClickable(true);

					final ImageView imagenOn = new ImageView(contexto);
					imagenOn.setImageBitmap(bitmapOn);

					StateListDrawable states = new StateListDrawable();
					states.addState(new int[] { android.R.attr.state_pressed },
							imagenOn.getDrawable());
					states.addState(new int[] { android.R.attr.state_focused },
							imagenOn.getDrawable());
					states.addState(new int[] {}, imagenOff.getDrawable());
					imagenOff.setImageDrawable(states);

					if (i == 0) {
						lineaItemInflado.setVisibility(View.GONE);
						imagenLayout.requestFocus();
						imagenOff.requestFocus();

					}
					Resources res = contexto.getResources();
					final Drawable drawable = res
							.getDrawable(R.drawable.foc_cuadro);

					if (imagenOff.isFocused()) {

						imagenLayout.setBackgroundDrawable(drawable);
					}

					imagenOff
							.setOnFocusChangeListener(new OnFocusChangeListener() {

								@Override
								public void onFocusChange(View v,
										boolean hasFocus) {

									if (hasFocus) {
										imagenLayout
												.setBackgroundDrawable(drawable);

									} else {
										imagenLayout
												.setBackgroundDrawable(null);
									}

								}
							});

					imagenOff.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

//							Toast.makeText(contexto, "Accediendo a widget: " + nombreIcono, Toast.LENGTH_SHORT).show();
							//webViewWidget
							//contexto.webViewWidget.LoadData();
							WebView _webView = (WebView) ((Activity) contexto).findViewById(R.id.webViewWidget);


							if (nombreIcono == "Widget1")
							{
								if (_webView.getVisibility() == View.GONE)
								{
									_webView.clearView();
//									_webView.loadData("<html id=" + (int)System.currentTimeMillis() / 1000 + "><body style=\"margin:0px; background-color:#006699EE;\"><div><p>" + nombreIcono + "</p></div></body></html>", "text/html; charset=UTF-8", null);
									_webView.setBackgroundColor(0xDDFFFFFF);
									_webView.setVisibility(View.VISIBLE);
									_webView.getSettings().setJavaScriptEnabled(true);
									_webView.getSettings().setPluginsEnabled(true);
									_webView.getSettings().setAllowFileAccess(true);
//									_webView.getSettings().setUserAgent(1);
									_webView.getSettings().setRenderPriority(RenderPriority.HIGH);
									_webView.getSettings().setAppCacheEnabled(false);
									_webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
																					
									_webView.loadUrl("http://130.206.83.123/Sensores/index.html");
									
									
								}
								else
									_webView.setVisibility(View.GONE);
							}
							else if (nombreIcono == "Widget2")
							{
								Intent intent = new Intent(contexto, WidgetSecurity.class);
								contexto.startActivity(intent);								
							}
							else if (nombreIcono == "Widget3")
							{
								// ...
							}


							
//							WidgetVistas.widget = getWidgetByList(widgetList,
//									nombreIcono);//
//
//							// verificando si existe un parametro que tenga
//							// el
//							// valor del
//							// nombre del widget
//							if (bundle != null) {
//								String parametro = bundle
//										.getString(WidgetVistas.widget
//												.getNombre());
//								if (parametro != null) {
//									Log.d(TAG, "PARAMETRO [" + parametro + "]");
//									WidgetVistas.widget.setParams(parametro);
//
//								}
//							}
//
//							Intent intent = new Intent(contexto,
//									WidgetVistas.class);
//							contexto.startActivity(intent);
						}
					});

					pos += 2;
//					pos = i;

					layoutScroll.addView(itemInflate);
				}
				
				WebView _webView = (WebView) ((Activity) contexto).findViewById(R.id.webViewWidget);
				_webView.setVisibility(View.GONE);


				// quitamos distractor
				((ProgressBar) ((WidgetsPortalActivity) this.contexto)
						.findViewById(R.id.distractor))
						.setVisibility(View.INVISIBLE);

			} catch (WidgetException e) {
				Log.e(TAG, "error de widget", e);
				WidgetsPortalActivity activity = (WidgetsPortalActivity) contexto;
				activity.muestraDialog();
			} catch (Exception e) {
				Log.e(TAG, "##### ERROR IconWidgetAsync.onPostExecute", e);
				WidgetsPortalActivity activity = (WidgetsPortalActivity) contexto;
				activity.muestraDialog();
			}

		}
	}

	private void zoomInIcon(View v) {
		Animation zoom_icon_in = AnimationUtils.loadAnimation(contexto,
				R.anim.zoom_icons_in);
		zoom_icon_in.setFillAfter(true);
		v.startAnimation(zoom_icon_in);
	}

	private void zoomOutIcon(View v) {
		Animation zoom_icon_out = AnimationUtils.loadAnimation(contexto,
				R.anim.zoom_icons_out);
		zoom_icon_out.setFillAfter(true);
		v.startAnimation(zoom_icon_out);
	}

	/**
	 * Este metodo me retornara un objeto de tipo Widget extraido de un listado
	 * a partir del nombre del icono del Widget
	 * 
	 * @param widgets
	 *            lista de objetos de tipo Widget
	 * @param nombreIcono
	 *            : nombre del icono , el nombre del icono es unico por widget
	 * @return
	 */
	private Widget getWidgetByList(List<Widget> widgets, String nombreIcono) {

		Widget widget = null;
		if (!widgets.isEmpty()) {
			for (Widget w : widgets) {
				if (w.getIcono().equals(nombreIcono)) {
					widget = w;
					Log.d(TAG, " " + widget.getIcono());
					break;
				}

			}
		}
		return widget;
	}

}
