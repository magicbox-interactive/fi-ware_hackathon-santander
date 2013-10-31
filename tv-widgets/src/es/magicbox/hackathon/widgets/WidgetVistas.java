package es.magicbox.hackathon.widgets;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.amlogic.dvb.DVBClient;
import es.magicbox.hackathon.widgets.R;

import es.magicbox.hackathon.background.display.manager.BackgroudDisplayManager;
import es.magicbox.hackathon.widgets.async.DownloadFileAsync;
import es.magicbox.hackathon.widgets.gestion.Utils;
import es.magicbox.hackathon.widgets.modelo.Vista;
import es.magicbox.hackathon.widgets.modelo.Widget;

/**
 * Actividad que muestra las distintas vistas de un widget
 * 
 * 
 */
public class WidgetVistas extends Activity {

	/**
	 * Tag usado para los Log
	 */
	private static final String TAG = WidgetsPortalActivity.class.getSimpleName();
	/**
	 * Numero de segundos en un minuto
	 */
	private static final int SECS_IN_MIN = 60;
	/**
	 * Numero de milisegundos en un segundo
	 */
	protected static final int MILS_IN_SEC = 1000;

	/**
	 * constante que indica el indice de la pagina back
	 */
	private final int PAGINA_BACK = 2;
	/**
	 * Textview para la fecha
	 */
	private TextView fecha;
	/**
	 * Textview para la hora
	 */
	private TextView hora;

	/**
	 * Objeto comun de la aplicacion
	 */
	protected CommonApp commonApp;
	/**
	 * Widget seleccionado en el menu
	 */
	public static Widget widget = null;
	/**
	 * Manejador con el que actualizar la hora y la fecha
	 */
	private final Handler manejadorHora = new Handler();

	/**
	 * Cliente DVB
	 */
	private DVBClient dvbClient = null;

	/**
	 * Webview de la parte izquierda
	 */
	private WebView webviewIzq;

	/**
	 * Webview de la parte derecha
	 */
	private WebView webviewDcha;

	/**
	 * Webview de la parte centro
	 */
	private WebView webviewCentro;

	/**
	 * vistas del widget seleccionado
	 */
	private List<Vista> vistas;

	/**
	 * siguiente posicion del elemento de la lista "vistas"
	 */
	private int posicionVistaSgte = 0;
	/**
	 * posicion del elemento de la lista "vistas"
	 */
	private int posicionVista = 0;

	/**
	 * tipo de vista en la que se encuentra actualmente la actividad
	 */
	private int tipoVista = 0;

	/**
	 * Si alguna url contiene esta cadena cargaremos la url en el mismo webview.
	 */
	private static final String NO_WINDOW = "nowindow=true";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "INICIO onCreate");
		try {
			super.onCreate(savedInstanceState);

			// Recuperamos el objeto comun
			commonApp = ((CommonApp) ((Activity) this).getApplication());

			if (Utils.isNetworkAvailable(getApplicationContext())) {
				getVistasWidget();
			} else {
				finish();
			}

			this.dvbClient = new DVBClient(this);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		Log.v(TAG, "FIN onCreate");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		Log.v(TAG, "Start onResume()");
		try {			
			
			if (!this.dvbClient.isConnected()) {
				this.dvbClient.connect();
			}
			if (!this.dvbClient.isPlaying()) {
				dvbClient.playByServiceID(dvbClient.getCurrentServiceID());
			}
			super.onResume();

			// TODO : PONER EL FOCO EN ALGUN WEBVIEW DEL LAYOUT QUE SE CARGUE

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

		Log.v(TAG, "Stop onResume()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		Log.v(TAG, "Start onPause()");
		super.onPause();
		finish();
		// Paramos la tarea asincrona que actualiza la hora
		this.manejadorHora.removeCallbacks(mUpdateTimeTask);
		Log.v(TAG, "Stop onPause()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		Log.v(TAG, "Start onDestroy()");
		try {
			super.onDestroy();
			Log.d(TAG, "***** Destruir Webviews *****");

			switch (tipoVista) {
			case Vista.TYPE_SIMPLE:
				WebView webviewOverLay = (WebView) findViewById(R.id.webview_overlay);
				webviewOverLay.destroy();
				break;
			case Vista.TYPE_ELE:
				WebView webviewIzq = (WebView) findViewById(R.id.webview_izquierdo);
				WebView webviewCentro = (WebView) findViewById(R.id.webview_centro);
				WebView webviewDcha = (WebView) findViewById(R.id.webview_derecho);
				webviewIzq.destroy();
				webviewCentro.destroy();
				webviewDcha.destroy();
				break;
			case Vista.TYPE_FULL_SCREEN:
				WebView webViewFullScreen = (WebView) findViewById(R.id.webview_full_screen);
				webViewFullScreen.destroy();
				break;

			default:
				Log.d(TAG, "No existe ninguna vista ");
				break;
			}

		} catch (Exception e) {
			Log.e(TAG, "Error al destruir webviews", e);
		}
		Log.v(TAG, "Stop onDestroy()");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.v(TAG, "Start onKeyDown");
		boolean result = true;
		try {
			// Boton f12
			if (KeyEvent.KEYCODE_BUTTON_R2 == keyCode) {
				finish();
				result = false;
			// Boton f11
			} else if (KeyEvent.KEYCODE_BUTTON_R1 == keyCode) {
				if (Utils.isNetworkAvailable(getApplicationContext())) {
					// Cambia entre los dos tipos de vista del widget
					getVistasWidget();
				} else {
					finish();
				}
				result = false;
			} else if (KeyEvent.KEYCODE_BACK == keyCode){
				WebView webview = null;
				switch (tipoVista) {
				case Vista.TYPE_SIMPLE:
					webview = (WebView) findViewById(R.id.webview_overlay);					
					break;
				case Vista.TYPE_ELE:
					webview = (WebView) findViewById(R.id.webview_izquierdo);				
					break;
				case Vista.TYPE_FULL_SCREEN:
					webview = (WebView) findViewById(R.id.webview_full_screen);					
					break;
				}
				 if(webview.canGoBack()){
					 webview.goBack();
					 result = false;
				 }else{
					 result = super.onKeyDown(keyCode, event);
				 }
			}else {
				Log.d(TAG, "FOCO: " + getCurrentFocus());
				Log.d(TAG, "KeyEvent: " + keyCode);
				result = super.onKeyDown(keyCode, event);
			}
		} catch (Exception e) {
			Log.e(TAG, "Error onKeyDown ", e);
		}
		Log.v(TAG, "Stop onKeyDown");
		return result;
	}

	/**
	 * Metodo que obtiene todas las vistas de un widget
	 * 
	 * @param widget
	 *            Widget
	 */
	private void getVistasWidget() {
		Log.d(TAG, "Inicio getVistasWidget()");
		try {
			this.vistas = widget.getVistas();
			int tamanioLista = vistas.size();

			if (tamanioLista == 1) {
				// Si el widget solo tiene una vista, la muestra
				setVistas(vistas.get(posicionVista));
				TextView cambiar_vista = (TextView)findViewById(R.id.barra_amarilla);
				cambiar_vista.setVisibility(View.GONE);
			} else {
				// Si el widget tiene varias vistas
				if (!vistas.isEmpty() && tamanioLista > 1) {

					for (int i = 0; i < vistas.size(); i++) {

						// cuando la siguiente posicion sea = que el tamanio de
						// la lista se reinicia la posicion sgte a la primera
						if (posicionVistaSgte == tamanioLista) {
							posicionVistaSgte = 0;

						}

						if (posicionVistaSgte == i) {
							posicionVista = i;
							posicionVistaSgte++;
							break;
						}
					}
					setVistas(vistas.get(posicionVista));
					TextView cambiar_vista = (TextView)findViewById(R.id.barra_amarilla);
					cambiar_vista.setVisibility(View.VISIBLE);
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Error en getVistasWidget()", e);
		}
		Log.d(TAG, "Fin getVistasWidget()");
	}

	/**
	 * Dada una vista selecciona el layout correspondiente para mostrarla y
	 * carga los elementos
	 * 
	 * @param vista
	 *            Vista a mostrar
	 */
	private void setVistas(Vista vista) {
		Log.d(TAG, "Inicio setVistas()");

		// parametros recibidos por el intent que invoco a la actividad
		// WidgetsPortalActivity, que serviran para concatenar a las url en caso
		// de no ser vacia
		String paramsUrl = (widget.getParams() != null && !widget.getParams().isEmpty()) ? widget.getParams() : "";
		try {
			int type = vista.getType();

			// asignamos el valor a la variable global para tener el control de
			// la vista que se esta mostrando actualmente en la actividad
			this.tipoVista = type;

			switch (type) {
			case Vista.TYPE_SIMPLE:
				Log.d(TAG, "Tipo de vista SIMPLE");
				setContentView(R.layout.widget_vertical_overlay);
				BackgroudDisplayManager.setVideoFullScreen();
				ImageView iconTab = (ImageView) findViewById(R.id.icon_tab);
				try {
					Bitmap bmp = new DownloadFileAsync().execute(widget.getIconoTab()).get();
					iconTab.setImageBitmap(bmp);
				} catch (InterruptedException e) {
					Log.e(TAG, "Error al obtener el icono del widget", e);
				} catch (ExecutionException e) {
					Log.e(TAG, "Error al obtener el icono del widget", e);
				}
				this.manejadorHora.removeCallbacks(mUpdateTimeTask);
				WebView webviewOverLay = (WebView) findViewById(R.id.webview_overlay);

				// Añadir parametros para peticion de url
				String urlIzqda = vista.getWebviewizquierda().trim() + paramsUrl;
				Log.d(TAG, "URL TWITTERS: " + urlIzqda);
				setWebViewsContent(webviewOverLay, urlIzqda);
				break;
			case Vista.TYPE_ELE:
				Log.d(TAG, "Tipo de vista ELE");
				setContentView(R.layout.widget_ele_overlay);
				VideoView view = (VideoView) findViewById(R.id.video_view);
				fecha = (TextView) findViewById(R.id.fecha);
				hora = (TextView) findViewById(R.id.hora);
				BackgroudDisplayManager.setVideoOnViewSurface(view);

				ImageView iconTabEle = (ImageView) findViewById(R.id.icon_tab);
				try {
					Bitmap bmp = new DownloadFileAsync().execute(widget.getIconoTab()).get();
					iconTabEle.setImageBitmap(bmp);
				} catch (InterruptedException e) {
					Log.e(TAG, "Error al obtener el icono del widget", e);
				} catch (ExecutionException e) {
					Log.e(TAG, "Error al obtener el icono del widget", e);
				}

				// indicamos que el video no sea focusable
				view.setFocusable(false);
				this.manejadorHora.post(mUpdateTimeTask);
				webviewIzq = (WebView) findViewById(R.id.webview_izquierdo);
				// Añadir parametros para peticion de url
				String urlIzq = vista.getWebviewizquierda().trim() + paramsUrl;
				Log.d(TAG, "URL TWITTERS: " + urlIzq);
				setWebViewsContent(webviewIzq, urlIzq);

				webviewCentro = (WebView) findViewById(R.id.webview_centro);
				setWebViewsContent(webviewCentro, vista.getWebviewcentro().trim());

				webviewDcha = (WebView) findViewById(R.id.webview_derecho);
				setWebViewsContent(webviewDcha, vista.getWebviewderecha().trim());

				// añadimos el listener del foco al webView de la derecha
				webviewDcha.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							// muestraToast("CON FOCO!!!!!");
							((View) v.getParent()).setBackgroundColor(getResources().getColor(R.color.borde_foco));
						} else {
							// muestraToast("SIN FOCO!!!!!");
							((View) v.getParent()).setBackgroundColor(getResources().getColor(R.color.background));
						}
					}
				});

				// ponemos el foco en el webview de la derecha
				webviewDcha.requestFocus();
				webviewDcha.setClickable(true);

				break;
			case Vista.TYPE_FULL_SCREEN:
				Log.d(TAG, "Tipo de vista FULL_SCREEN");
				setContentView(R.layout.widget_full_screen);

				// validara si los parametros de la tv son validos para
				// redimensionarla y posicionarla.
				if (vista.isValidParamTv()) {
					BackgroudDisplayManager.setVideoWindow(vista.getPositionX(), vista.getPositionY(), vista.getWidth(), vista.getHeight());
				} else {
					BackgroudDisplayManager.setVideoFullScreen();
				}

				this.manejadorHora.removeCallbacks(mUpdateTimeTask);
				WebView webviewFullScreen = (WebView) findViewById(R.id.webview_full_screen);
				webviewFullScreen.requestFocus();
				// Añadir parametros para peticion de url
				String urlIzqdaTemp = vista.getWebviewizquierda().trim() + paramsUrl;
				Log.d(TAG, "URL TYPE_FULL_SCREEN " + urlIzqdaTemp);
				setWebViewsContent(webviewFullScreen, urlIzqdaTemp,vista.getWebZoom());

				if (vista.isValidParamWeb()) {
					RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.webFullScreem);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.leftMargin = vista.getWebX();
					params.topMargin = vista.getWebY();
					params.height = vista.getWebHeight();
					params.width = vista.getWebWidth();
					linearLayout.setLayoutParams(params);

				}
				break;
			default:
				Log.d(TAG, "Tipo de vista desconocido");
				break;
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
		Log.d(TAG, "Fin setVistas()");
	}

	/**
	 * Setea el contenido en los webviews
	 * 
	 * @param webview
	 *            Objeto WebView
	 * @param url
	 *            Url para cargar contenido
	 */
	private void setWebViewsContent(final WebView webview, final String url) {
		setWebViewsContent(webview, url, null);
	}

	/**
	 * Setea el contenido en los webviews
	 * 
	 * @param webview
	 *            Objeto WebView
	 * @param url
	 *            Url para cargar contenido
	 * @param webZoom
	 *            zoom que se aplica a la webview puede ser , FAR (alejar) ,
	 *            MEDIUM (normal) y CLOSE (acercar)
	 * 
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void setWebViewsContent(final WebView webview, final String url, String webZoom) {
		Log.v(TAG, "Inicio setWebViewsContent() " + url);
		
		try {
			// Borra el cache de la aplicacion
			// Necesita permisos CLEAR_APP_CACHE
			webview.clearCache(true);
			// webview.setBackgroundColor(Color.TRANSPARENT);
			webview.setBackgroundColor(0x01010101);
			webview.getSettings().setJavaScriptEnabled(true);
	    	webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			webview.setFocusable(true);
			webview.setClickable(true);
			webview.getSettings().setPluginsEnabled(true);
			webview.getSettings().setAllowFileAccess(true);
//			webview.getSettings().setUserAgent(1);
			webview.getSettings().setRenderPriority(RenderPriority.HIGH);
			webview.getSettings().setAppCacheEnabled(false);
			webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
						
			// haciendo zoom al webview
			if (webZoom != null && !webZoom.isEmpty()) {
				if (webZoom.equals(ZoomDensity.FAR.toString())) {
					webview.getSettings().setDefaultZoom(ZoomDensity.FAR);
				}
				if (webZoom.equals(ZoomDensity.MEDIUM.toString())) {
					webview.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
				}
				if (webZoom.equals(ZoomDensity.CLOSE.toString())) {
					webview.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
				}
			}
			
			// Añadimos la interfaz de javascript
			JSInterface jsInterface = new JSInterface(webview, this);
			// Ejecuta los metodos de la interfaz desde codigo javascript
			webview.addJavascriptInterface(jsInterface, "JSInterface");
	    	
			webview.setWebViewClient(new WebViewClient() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * android.webkit.WebViewClient#shouldOverrideUrlLoading(android
				 * .webkit.WebView, java.lang.String)
				 */
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					try{
						// obtenemos el historial del webview
						WebBackForwardList lista = view.copyBackForwardList();
						// comprobamos que la url no se corresponde con un back
						if ((null == lista.getItemAtIndex(lista.getSize() - PAGINA_BACK))
								|| (null != lista.getItemAtIndex(lista.getSize() - PAGINA_BACK) && !lista.getItemAtIndex(lista.getSize() - PAGINA_BACK).getUrl()
										.equals(url))) {
							// Si hay video y no es una navegacion interna se para
							if (dvbClient.isConnected() && url != null && !url.contains(NO_WINDOW)
									&& !url.contains("mobile.twitter")) {
								if (dvbClient.isPlaying()) {
									dvbClient.stopPlaying();
									dvbClient.disconnect();
								}
							}
	
							if (url != null && url.startsWith("http") && !url.contains(NO_WINDOW) && !url.contains("mobile.twitter")) {
								view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
								return true;
	
							} else {
								return false;
							}
						}
						// sino cargamos la url
						else {
							// limpiamos la vista
							view.clearView();
							// cargamos la nueva url
							view.goBack();
							return true;
						}
	
					}catch (Exception e) {
						Log.e(TAG, "Error en redireccionamiento", e);
					}
					return false;
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see android.webkit.WebViewClient#onReceivedError
				 * (android.webkit.WebView, int, java.lang.String,
				 * java.lang.String)
				 */
				@Override
				public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
					webview.setVisibility(View.INVISIBLE);
					super.onReceivedError(view, errorCode, description, failingUrl);
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see android.webkit.WebViewClient#shouldOverrideKeyEvent
				 * (android.webkit.WebView, android.view.KeyEvent)
				 */
				@Override
				public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
					Log.v(TAG, "Start shouldOverrideKeyEvent");
					Log.d(TAG, "#KEY: " + event.getKeyCode());
					boolean result = true;
					try {
						// Boolean retorno = false;
						if (KeyEvent.KEYCODE_BUTTON_R2 == event.getKeyCode()) {
							finish();
							result = false;
						} else if (KeyEvent.KEYCODE_BUTTON_R1 == event.getKeyCode()) {
							if (Utils.isNetworkAvailable(getApplicationContext())) {
								getVistasWidget();
							} else {
								finish();
							}
							result = false;
						} else if (tipoVista != Vista.TYPE_FULL_SCREEN) {

							if (KeyEvent.KEYCODE_DPAD_LEFT == event.getKeyCode()) {
								if (!webviewIzq.hasFocus()) {
									webviewIzq.requestFocus();
								} else {
									result = super.shouldOverrideKeyEvent(view, event);
								}

							} else if (KeyEvent.KEYCODE_DPAD_DOWN == event.getKeyCode()) {
								if (webviewDcha.hasFocus()) {
									webviewCentro.requestFocus();
								} else {
									result = super.shouldOverrideKeyEvent(view, event);
								}

							} else if (KeyEvent.KEYCODE_DPAD_UP == event.getKeyCode()) {
								if (webviewCentro.hasFocus()) {
									webviewDcha.requestFocus();
								}

								else {
									result = super.shouldOverrideKeyEvent(view, event);
								}

							}
						} else {
							Log.d(TAG, "Se llama a super para gestion de evento");
							result = super.shouldOverrideKeyEvent(view, event);
						}
					} catch (Exception e) {
						Log.e(TAG, "Error shouldOverrideKeyEvent " + TAG);
					}
					return result;
				}

			});
			webview.loadUrl(url);
			
		} catch (Exception e) {
			Log.e(TAG, "Error cargando los webviews", e);
		}
		Log.v(TAG, "Fin setWebViewsContent()");
	}

	/**
	 * Actualiza la hora cada minuto
	 */
	private final Runnable mUpdateTimeTask = new Runnable() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			Log.v(TAG, "Inicio traza tiempo");
			Date fechaDate = new Date();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			hora.setText(format.format(fechaDate));
			format = new SimpleDateFormat("dd/MM/yyyy");
			fecha.setText(format.format(fechaDate));
			long mils = System.currentTimeMillis();
			long delay = (SECS_IN_MIN - (mils / MILS_IN_SEC % SECS_IN_MIN)) * MILS_IN_SEC - mils % MILS_IN_SEC;
			Log.d(this.getClass().toString(), "Hora: " + new Date(mils) + " DELAY: " + delay);
			manejadorHora.postDelayed(this, delay);
			Log.v(TAG, "Fin traza tiempo - Delay: " + delay);
		}
	};

}
