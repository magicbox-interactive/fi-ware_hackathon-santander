package es.magicbox.hackathon.widgets.gestion;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import es.magicbox.hackathon.widgets.CommonApp;
import es.magicbox.hackathon.widgets.exception.WidgetException;
import es.magicbox.hackathon.widgets.modelo.Widget;

/**
 * Controlador encargado de la gestión del parseamiento y obtención de los XMLS
 * de la blacklist de mis aplicaciones
 *
 */
public class GestorWidgets {

	/**
	 * Contexto de la activity
	 */
	private final Context contexto;
	/**
	 * Manejador de XML lista DOM
	 */
	private ManejadorXMLwidgetDOM manejadorXMLwidgetDOM;	
	/**
	 * Fichero remoto
	 */
	private static final int TIPO_REMOTO = 1;
	/**
	 * Fichero local
	 */
	private static final int TIPO_LOCAL = 2;
	/**
	 * TAG para los logs
	 */
	private static final String TAG = GestorWidgets.class.getSimpleName();

	/**
	 * Constructor de la clase
	 * 
	 * @param contexto Contexto
	 */
	public GestorWidgets(Context contexto) {
		this.contexto = contexto;
		manejadorXMLwidgetDOM = new ManejadorXMLwidgetDOM(this.contexto);
	}
	
	
	/**
	 * Obtiene el contenido del XML
	 * 
	 * @param tipoFichero Tipo de fichero, local o remoto
	 * @param pathRemoto Path remoto del fichero
	 * 
	 * @return Lista de widgets contenidos en el XML
	 */
	public List<Widget> getContenidoWidgets(int tipoFichero, String pathRemoto) {
		Log.v(TAG, "INICIO getContenidoWidgets");
		List<Widget> listaAux = null;
		try {
			// Obtener desde remoto
			if (TIPO_REMOTO == tipoFichero) {
				listaAux = manejadorXMLwidgetDOM.parseaWidget(pathRemoto);				
			} else if (TIPO_LOCAL == tipoFichero) {				
				if (Utils.existeFichero(CommonApp.PATH_WIDGETS_LOCAL)) {
					
					listaAux = new ArrayList<Widget>();
					
					listaAux = this.manejadorXMLwidgetDOM.parseaWidget(CommonApp.PATH_WIDGETS_LOCAL);

					// Si la lista es NULL descargamos de ASSETS
					if (null == listaAux) {						
						listaAux = obtieneXMLassets();
					} 
				} else {
					listaAux = obtieneXMLassets();					
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Error parseando Widgets ", e);		
			listaAux = null;
		} finally {
			Log.v(TAG, "Finally - getContenidoWidgets");
		}

		Log.v(TAG, "FIN getContenidoWidgets");
		return listaAux;
	}
	
	
	

	/**
	 * Guarda el XML desargado en el directorio correspondiente
	 * 
	 * @param url Url de descarga del XML
	 * @param listado Lista de widgets
	 * 
	 * @return true si se ha guardado correctamente
	 */
	public boolean guardaXMLwidgets(String url, List<Widget> listado) {
		Log.v(TAG, "INICIO guardaXMLwidgets");
		boolean resultado = false;
		try {		
			// Gestionamos su contenido
			Utils.descargaWidgets(url, listado);
			resultado = true;
			Log.d(TAG, "guardaXMLwidgets OK");
		} catch (Exception e) {
			resultado = false;
			Log.d(TAG, "guardaXMLwidgets ERROR");
		}
		Log.v(TAG, "FIN guardaXMLwidgets");
		return resultado;
	}
	
	
	
	/**
	 * Metodo que obtiene los datos de un XML
	 * guardado en la carpeta assets del proyecto
	 * 
	 * @return Listado de Items
	 */
	public List<Widget> obtieneXMLassets() {
		Log.v(TAG, "INICIO obtieneXMLassets");
		String nombreFichero = "widgets.xml";
		List<Widget> widgetList = null;
		try {			
			// Obtengo el gestor de la carpeta Assets
			AssetManager assetManager = this.contexto.getResources()
					.getAssets();
		
			InputStream is = assetManager.open("xml/" + nombreFichero);
			if (null != is) {
				Log.v(TAG, "obtieneXMLassets: ANTES DE PARSEO ASSETS HOME "
						+ nombreFichero);
				widgetList = manejadorXMLwidgetDOM.parseaWidget(is);
				Log.v(TAG,
						"obtieneXMLassets: DESPUES DE PARSEO ASSETS HOME "
								+ nombreFichero);
			}
			
		} catch (Exception e) {
			Log.e(TAG, "Error a la hora de parsear la vista "
					+ nombreFichero + " tipo ASSETS");
			widgetList = null;
		}
		Log.v(TAG, "FIN obtieneXMLassets");
		return widgetList;
	}
	
	
	/**
	 * Método con descargar y parsear el XML de widgets. 
	 * Si tarda mas de CONNECTION_TIMEOUT_SECONDS salta una exception
	 * 
	 * @param path Path de descarga del XML
	 * @return List<item>
	 */
	public List<Widget> descargaXMLremotoWidgets(String path) throws WidgetException{
		List<Widget> widgetsList = null;
		Log.v(TAG, "INICIO descargaXMLremotoWidgets ");
		try {
			// Creo el servicio de acceso al XML remoto
			ServicioDescargaXMLwidgets service = ServicioDescargaXMLwidgets.create(
					this, path);
	
			Future<List<Widget>> future = service.getWidgetstRemoto();			
			List<Widget> wList = future.get(Utils.CONNECTION_TIMEOUT_SECONDS,TimeUnit.MILLISECONDS);			
			widgetsList = wList;
		} catch (Exception e) {
			//Log.e(TAG, "ERROR: descargaXMLremotoWidgets: Sin acceso al servidor ");
			throw new WidgetException("ERROR: descargaXMLremotoWidgets: Sin acceso al servidor ",e);
			//widgetsList = null;
		}
		Log.v(TAG, "FIN descargaXMLremotoWidgets ");
		return widgetsList;
	}
	
	
	/**
	 * Clase con la que descargar y parsear el XML de widgets,
	 * si tarda mas de un tiempo específico salta
	 * 
	 *
	 */
	private static class ServicioDescargaXMLwidgets {
		/**
		 * Executor encargado de ejecutar la descarga
		 */
		private ExecutorService executor;
		/**
		 * Gestor Vistas
		 */
		private GestorWidgets gw;
		
		/**
		 * Path remoto de la blacklist XML
		 */
		private String pathRemoto;

	
		/**
		 * Constructor del servicio de descarga del XML de mis_aplicaciones remoto
		 * 
		 * @param gw Gestor de widgets
		 * @param path Path remoto
		 */
		private ServicioDescargaXMLwidgets(GestorWidgets gw, String path) {
			this.gw = gw;
			this.pathRemoto = path;
			executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setDaemon(true);
					return t;
				}
			});
		}

		
		/**
		 * Método a través del cual crear el servicio
		 * 
		 * @param gw gestor de widgets
		 * @param path path de descarga de XML
		 * 
		 * @return ServicioDescargaXMLwidgets
		 */
		static ServicioDescargaXMLwidgets create(GestorWidgets gw, String path) {

			return new ServicioDescargaXMLwidgets(gw, path);
		}

		
		/**
		 * Método que obtiene la lista de items descargada y parseada a través del future
		 * 
		 * @return Future<List<Item>>
		 */
		Future<List<Widget>> getWidgetstRemoto() {
			FutureTask<List<Widget>> future = new FutureTask<List<Widget>>(new Callable<List<Widget>>() {
				@Override
				public List<Widget> call() throws Exception {
					return gw.getContenidoWidgets(TIPO_REMOTO, pathRemoto);
				}
			});
			executor.execute(future);
			return future;
		}
	}
	
	
	

	
}
