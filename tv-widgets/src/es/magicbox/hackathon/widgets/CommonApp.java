package es.magicbox.hackathon.widgets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import android.app.Application;
import android.util.Log;

/**
 * Clase que contiene objetos comunes para la aplicacion
 * 
 * 
 */
public class CommonApp extends Application {

	/**
	 * TAG de logs
	 */
	public static final String TAG = CommonApp.class.getSimpleName();

	/**
	 * Mapa con las rutas en el caso de que se este en modo debug
	 */
	private Map<String, String> rutasDebug;
	/**
	 * Descripcion a mostrar en el modo debug
	 */
	private String modoDebug = null;
	/**
	 * Variable booleana que indica si estamos en modo debug o no
	 */
	private Boolean enModoDebug = false;
	/**
	 * Clave DescripcionF
	 */
	public static final String DESCRIPCION = "DESCRIPCION";

	/**
	 * Path donde guardar el xml en local
	 */
	public static final String PATH_WIDGETS_LOCAL = "/data/data/es.magicbox.hackathon.widgets/widgets/";

	/**
	 * Mapa de parametros
	 */
	private Map<String, List<String>> mapaParams = new HashMap<String, List<String>>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
	}

	/**
	 * Método a través del cual obtener si estamos en modo debug o no
	 * 
	 * @return the enModoDebug
	 */
	public Boolean getEnModoDebug() {
		Log.d(TAG, "Start + Stop getEnModoDebug()");
		return enModoDebug;
	}

	/**
	 * Método con el que setear si estamos en modo debug o no
	 * 
	 * @param enModoDebug
	 *            - the enModoDebug to set
	 */
	private void setEnModoDebug(Boolean enModoDebug) {
		Log.d(TAG, "Inicio setEnModoDebug()");
		this.enModoDebug = enModoDebug;
		Log.d(TAG, "Fin setEnModoDebug()");
	}

	/**
	 * Devuelve una lista de parametros a partir de su clave
	 * 
	 * @param key
	 *            - Clave
	 * @return Lista de parametros
	 */
	public List<String> getParamsListado(String key) {
		Log.v(TAG, "INICIO getParamsListado " + key);
		List<String> bList = null;
		if (this.mapaParams.containsKey(key)) {
			bList = this.mapaParams.get(key);
		}
		Log.v(TAG, "FIN getParamsListado " + key);
		return bList;
	}

	/**
	 * Guarda listas de parametros en un mapa de valores
	 * 
	 * @param listParams
	 *            - Lista de parametros
	 * @param key
	 *            - Clave de la lista
	 */
	public void addParamsListado(List<String> listParams, String key) {
		Log.v(TAG, "INICIO addParamsListado");
		if (this.mapaParams.containsKey(key)) {
			this.mapaParams.remove(key);
		}
		this.mapaParams.put(key, listParams);
		Log.v(TAG, "FIN addParamsListado");
	}

}
