package es.magicbox.hackathon.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.amlogic.dvb.DVBClient;
import com.amlogic.dvb.DVBClient.OnEventListener;
import com.amlogic.dvb.DVBEvent;

import es.magicbox.hackathon.background.display.manager.BackgroudDisplayManager;
import es.magicbox.hackathon.widgets.async.WidgetAsync;
import es.magicbox.hackathon.widgets.gestion.Utils;

/**
 * Actividad con los widgets del portal muestra una barra con todos los widgets
 * para acceder al contenido
 * 
 * 
 */
public class WidgetsPortalActivity extends Activity {
	/**
	 * Tag usado para los Log
	 */
	private static final String TAG = WidgetsPortalActivity.class
			.getSimpleName();
	
	/**
	 * Objeto comun de la aplicacion
	 */
	protected CommonApp commonApp;

	/**
	 * Cliente DVB
	 */
	private DVBClient dvbClient = null;	

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "Start onCreate");
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.menu_widgets);

			// Recuperamos el objeto comun
			this.commonApp = ((CommonApp) ((Activity) this).getApplication());			

			// inicializo dvb
			this.dvbClient = new DVBClient(this);
			this.dvbClient.setEventListener(dvbEventListener);
			this.dvbClient.connect();

			//si hay red recuperamos los widgets y los pintamos
			if (Utils.isNetworkAvailable(getApplicationContext())) {
				new WidgetAsync(WidgetsPortalActivity.this, this.getIntent()
						.getExtras()).execute();
			} else {
				muestraDialog();
			}

		} catch (Exception e) {
			Log.e(TAG, "Error onCreate " + TAG);
		}
		Log.v(TAG, "Stop onCreate");
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
			BackgroudDisplayManager.setVideoFullScreen();
			super.onResume();
		} catch (Exception e) {
			Log.e(TAG, "Error onResume " + TAG);
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
		Log.v(TAG, "Stop onPause()");
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
				
			} else if (KeyEvent.KEYCODE_BUTTON_L1 == keyCode){
				RelativeLayout _menu = (RelativeLayout)this.findViewById(R.id.menu);
				WebView _webView = (WebView) this.findViewById(R.id.webViewWidget);
				if (_menu.getVisibility() == View.GONE)
				{
					_menu.setVisibility(View.VISIBLE);
//					_webView.setVisibility(View.VISIBLE);
////					_webView.loadData("<html id=" + (int)System.currentTimeMillis() / 1000 + "><body style=\"margin:0px; background-color:#006699EE;\"><div><p>" + "http://1" + "</p></div></body></html>", "text/html; charset=UTF-8", null);
//					_webView.getSettings().setJavaScriptEnabled(true);
//					_webView.getSettings().setPluginsEnabled(true);
//					_webView.getSettings().setAllowFileAccess(true);
////					_webView.getSettings().setUserAgent(1);
//					_webView.getSettings().setRenderPriority(RenderPriority.HIGH);
//					_webView.getSettings().setAppCacheEnabled(false);
//					_webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//																	
//					_webView.loadUrl("http://130.206.83.123/Sensores/index.html");
//					_webView.setBackgroundColor(0xDDFFFFFF);
				}
				else
				{
					_menu.setVisibility(View.GONE);
					_webView.setVisibility(View.GONE);
				}
				
				
			} else {
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
	 * Muestra un toast con el texto indicado
	 * 
	 * @param texto
	 *            Mensaje a mostrar
	 * 
	 */
	public void muestraToast(String texto) {
		Log.v(TAG, "Inicio metodo muestraToast(String texto)");
		Toast toastInfo = null;
		try {
			// Si no se ha iniciado el toast, se precarga el toast informativo
			if (null == toastInfo) {
				LayoutInflater inflater = (LayoutInflater) this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.toastorbit,
						(ViewGroup) findViewById(R.id.toast_layout_root));
				toastInfo = new Toast(this);
				toastInfo.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toastInfo.setDuration(Toast.LENGTH_LONG);
				toastInfo.setView(layout);
			}
			// Se asigna el texto y se lanza el show
			((TextView) ((LinearLayout) toastInfo.getView()).getChildAt(1))
					.setText(texto);
			toastInfo.show();
		} catch (Exception e) {
			Log.e(TAG, "Erros al mostrar el Toast", e);
		}
		Log.v(TAG, "Fin metodo muestraToast(String texto)");
	}

	/**
	 * Dialogo que se mostrara si hay algun error al descargar los xml de widget
	 * y errores de conexion
	 */
	public void muestraDialog() {

		// hacemos invisible el layout MENU
		RelativeLayout layoutPrincipal = (RelativeLayout) findViewById(R.id.menu);
		layoutPrincipal.setVisibility(RelativeLayout.INVISIBLE);
		// hacemos invisible el video
		VideoView videoView = (VideoView) findViewById(R.id.video);
		videoView.setVisibility(VideoView.INVISIBLE);

		LayoutInflater mInflater;
		mInflater = LayoutInflater.from(this);
		View miview = new View(this);
		miview = mInflater.inflate(R.layout.dialog_marca, null);

		final Dialog dialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		dialog.setCancelable(false);
		dialog.setContentView(miview, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		dialog.show();
		TextView aceptar = (TextView) miview.findViewById(R.id.txtAceptar);

		aceptar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Cerramos el dialogo
				dialog.cancel();
				// matamos la actividad
				finish();
			}
		});
	}

	/**
	 * Listener de dvb, controla eventos dle mismo
	 */
	private OnEventListener dvbEventListener = new OnEventListener() {

		@Override
		public void onEvent(DVBClient c, DVBEvent e) {

		}

		@Override
		public void onDisconect(DVBClient dvbClient) {

		}

		@Override
		public void onConnected(DVBClient c) {
			if (!dvbClient.isPlaying()) {
				dvbClient.playByServiceID(dvbClient.getCurrentServiceID());
			}

		}
	};

}
