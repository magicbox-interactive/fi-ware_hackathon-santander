package es.magicbox.hackathon.background.display.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.widget.VideoView;

/**
 * Clase usada para cambiar la posicion del framebuffer de fondo
 * 
 * 
 */
public final class BackgroudDisplayManager extends Activity {

	/**
	 * TAG para logs
	 */
	private static final String TAG = BackgroudDisplayManager.class
			.getSimpleName();

	/**
	 * Notacion en pixeles
	 */
	public static final int PX = 0;

	/**
	 * Notacion en densidad por pixel
	 */
	public static final int DP = 0;

	/**
	 * Videoview
	 */
	private static VideoView view = null;

	/**
	 * Constructor privado para hacer la clase singleton
	 */
	private BackgroudDisplayManager() {
		// CLASE SINGLENNTON
	}

	/**
	 * 
	 * Funcion que pone la ventana del video en la posicion x e y, con el ancho
	 * y alto pasado en pixeles
	 * 
	 * @param x
	 *            coordenada x en pixeles
	 * @param y
	 *            coordenada y en pixeles
	 * @param w
	 *            ancho en pixeles
	 * @param h
	 *            alto en pixeles
	 */
	public static void setVideoWindow(int x, int y, int w, int h) {
		Log.d(TAG, "start setVideoPosition");

		try {
			// Escribimos la linea de configuracion
			Log.d(TAG, "tamanios" + " " + x + " " + y + " " + w + " " + h);

			// Establecemos la posicion de la caja de video
			writeStringInFile("/sys/class/video/axis",0+" "+0+" "+w+" "+h);			

			// La desplazamos a la posicion deseada
			writeStringInFile("/sys/class/video/global_offset", x + " " + y);
		} catch (Exception e) {
			Log.e(TAG, "Error en setVideoPosition", e);
		}

		Log.d(TAG, "stop setVideoPosition");
	}

	/**
	 * Metodo que posiciona el video de fondo en la posicion que ocupa una vista
	 * dada
	 * 
	 * @param v
	 *            vista
	 * @return true vista encontrada y video de fondo configurado false vista no
	 *         encontrada o error en proceso
	 */
	public static boolean setVideoOnViewSurface(VideoView v) {
		Log.d(TAG, "start setVideoOnViewSurface");
		boolean retorno = false;

		view = v;

		// Si existe vista de video
		if (null != view) {
			Log.d(TAG, "vista con video ");
			
			BackgroudDisplayManager.putVideoInView(view.getHolder());
			//TODO para discriminar, que la funcion putVideo devuelva 1 o 0 en funcion de si esta establecido o no?
			view.getHolder().addCallback(videoViewCallback);		
			
		}
		Log.d(TAG, "stop setVideoOnViewSurface");
		return retorno;
	}

	/**
	 * Funcion que pone el video de fondo a full screen
	 * 
	 */
	public static void setVideoFullScreen() {
		Log.d(TAG, "start setVideoFullScreen");
		String cadenas[] = readStringInFile(
				"/sys/class/video/device_resolution").split("x");
		Log.d(TAG, "cadenas" + cadenas.toString());
		// ponemos la resolucion fullscreen del dispositivo
		BackgroudDisplayManager.setVideoWindow(0, 0,
				Integer.parseInt(cadenas[0]), Integer.parseInt(cadenas[1]));

		Log.d(TAG, "stop setVideoFullScreen");
	}
	
	/**
	 * Funcion que pone el video de fondo a full screen esperand miliseconds
	 * @param milis tiempo en milisegundos
	 */
	public static void setVideoFullScreenDelayed(int milis) {
		Log.d(TAG, "start setVideoFullScreen");
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				String cadenas[] = readStringInFile(
						"/sys/class/video/device_resolution").split("x");
				Log.d(TAG, "cadenas" + cadenas.toString());
				// ponemos la resolucion fullscreen del dispositivo
				BackgroudDisplayManager.setVideoWindow(0, 0,
						Integer.parseInt(cadenas[0]), Integer.parseInt(cadenas[1]));
				
			}
		}, milis);
		Log.d(TAG, "stop setVideoFullScreen");
	}

	/**
	 * @param path
	 *            path del fichero a escribir
	 * @param string
	 *            cadena a escribir en fichero
	 * @return true si ok, false si nok
	 */
	private static boolean writeStringInFile(String path, String string) {
		Log.d(TAG, "start writeStringInFile " + path + " " + string);
		Boolean retorno = false;

		try {
			// Abrimos buffer de escritura
			BufferedWriter out = new BufferedWriter(new FileWriter(path), 32);

			// Escribimos en el fichero
			try {
				out.write(string);
				retorno = true;
			} catch (IOException e) {
				Log.e(TAG, "error escribiendo en fichero " + path);
			} finally {
				// cerramos el fichero siempre
				out.close();
			}
		} catch (IOException e) {
			Log.e(TAG, "error abriendo fichero " + path);
		}

		Log.d(TAG, "stop writeStringInFile return " + retorno);
		return retorno;
	}

	/**
	 * Funcin para leer de ficher
	 * 
	 * @param path
	 *            a fichero a leer
	 * @return texto leido o null si no leido
	 */
	private static String readStringInFile(String path) {
		Log.d(TAG, "start readStringInFile " + path);
		String retorno = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			try {
				retorno = in.readLine();
			} finally {
				in.close();
			}
		} catch (IOException e) {
			Log.e(TAG, "IOException lectura fichero: " + path);
		}
		Log.d(TAG, "stop readStringInFile " + retorno);
		return retorno;
	}

	/**
	 * Callback llamado cuando el surface holder del video cambia de estado
	 * Podemos saber cuando esta disponible y extraer sus parametros de posicion
	 * y tamaño
	 */
	private static Callback videoViewCallback = new Callback() {

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Log.d(TAG, "start surfaceCreatedvideo videoViewCallback");

			putVideoInView(holder);
			Log.d(TAG, "stop surfaceCreatedvideo videoViewCallback");
		}

		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Log.d(TAG, "start surfaceChangedvideo videoViewCallback");

		}
	};
	
	//TODO pordiox, poner un nombre de verdad
	/**
	 * Funcion que pone el video en la posicion de un holder
	 * Codigo separado de setvideoinsurface, ya que se duplica
	 * codigo
	 * @param holder que contiene el video
	 */
	private static void putVideoInView(SurfaceHolder holder) {
		Log.d(TAG,"start putVideoInViewSurface");
		// Background Video Settings
		int vvS[] = { 0, 0, 0, 0 };

		Rect vRect = holder.getSurfaceFrame();

		// Extraemos valores
		view.getLocationOnScreen(vvS);
		vvS[2] = vRect.right - vRect.left;
		vvS[3] = vRect.bottom - vRect.top;

		setVideoWindow(vvS[0], vvS[1], vvS[2], vvS[3]);
	}
}
