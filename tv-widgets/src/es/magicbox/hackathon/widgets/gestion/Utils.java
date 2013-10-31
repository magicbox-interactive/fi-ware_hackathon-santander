package es.magicbox.hackathon.widgets.gestion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import es.magicbox.hackathon.widgets.CommonApp;
import es.magicbox.hackathon.widgets.modelo.Widget;

/**
 * Clase de utilidades
 * 
 */
public class Utils {
	/**
	 * TAG de los logs
	 */
	private static final String TAG = Utils.class.getSimpleName();
	/**
	 * Nombre del fichero
	 */
	public static final String FILENAME = "widgets.xml";
	/**
	 * Timeout para la conexion con el servidor
	 */
	public static final int CONNECTION_TIMEOUT_SECONDS = 4000;	
	

	
	/**
	 * Clave para los parametros de twitter
	 */
	public static final String TWITTER_KEY = "twitter";
	
	/**
	 * Obtiene el recurso de una fuente externa
	 * 
	 * @param sourceUrl url del recurso
	 * @return InputStream con el contenido
	 */
	public static InputStream getRemoteStream(String sourceUrl){
		Log.v(TAG,"Inicio metodo getRemotStream(String sourceUrl)");
		InputStream resultado = null;
		try{
			URLConnection http = null;
			URL url = new URL(sourceUrl);
			//Discriminamos conexiones https de http, se traga todo
			if (url.getProtocol().equalsIgnoreCase("https")) {
				
				//Si estoy en modo debug confio en TODOS
//				if(this.commonObject.getEnModoDebug()){
//						trustAllHosts();
//				}
				trustAllHosts();
				http = url.openConnection();
				((HttpsURLConnection)http).setHostnameVerifier(DO_NOT_VERIFY);
				
				//Si estoy en modo debug NO verifico NINGUNO
//				if(this.commonObject.getEnModoDebug()){
//					((HttpsURLConnection)http).setHostnameVerifier(DO_NOT_VERIFY);
//				}
				
				((HttpURLConnection)http).setRequestMethod("GET");
			
			} else if (url.getProtocol().equalsIgnoreCase("file")) {
				http =  url.openConnection();
			}else{
				http =  url.openConnection();
				((HttpURLConnection)http).setRequestMethod("GET");
			}			
			
			//Definimos metodo, salida y timeout
			http.setDoOutput(true);
			http.setConnectTimeout(4000);
			resultado = http.getInputStream();
		}catch (IOException e) {
			Log.e(TAG, "getRemoteStream: Error en la descarga de: " + sourceUrl, e);
		}
		Log.v(TAG,"Fin metodo getRemotStream(String sourceUrl)");
		//Creamos inputStream para poder leer los datos recibidos
		return resultado;
	}
	
	/**
	 * Trust every server - dont check for any certificate
	 */
	public static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
			.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	
	/**
	 * always verify the host - dont check for certificate
	 */
	public static final  HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};
	
	
	/**
	 * Guarda el listado de widgets en local
	 * 
	 * @param url url de descarga
	 * @param widgets Lista de widgets
	 */
	public static void descargaWidgets(String url, List<Widget> widgets){
		Log.v(TAG,"Inicio metodo descargaWidgets");
		try{		
			InputStream contenido = getRemoteStream(url);
			if(null != contenido){
				try{
					saveViewFile(FILENAME, contenido);								
				}catch (Exception e) {
					Log.e(TAG, "Error al guardar el contenido: " + FILENAME, e);								
				}
			}
						
		} catch (Exception e) {
			Log.e(TAG, "ERROR descargaWidgets ",e);

		}
		Log.v(TAG,"Fin metodo descargaWidgets");
	}
	
		
	/**
	 * Comprueba si existe el fichero
	 * 
	 * @param nameFile Nombre del fichero
	 * @return true si existe
	 */
	public static boolean existeFichero(String nameFile){
		File folder =  new File(nameFile);		
		if(!folder.exists()){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * Guarda un Inputstream en el fichero
	 * 
	 * @param fileName Nombre del fichero
	 * @param content Inputstream con el contenido
	 */
	public static void saveViewFile(String fileName, InputStream content){
		// Se crea el file
		File folder =  new File(CommonApp.PATH_WIDGETS_LOCAL);
		// Si el directorio no existe se crea
		if(!folder.exists()){
			folder.mkdir();
		}
		File file =  new File(folder +"/"+FILENAME);
		// Si el directorio no existe se crea
		
		FileOutputStream fileOutput =null;
		try {
			// Se crea el file output
			fileOutput = new FileOutputStream(file);
			
			// creamos un buffer...
			byte[] buffer = new byte[1024];
			// Se alamcena el tamaño del buffer
			int bufferLength = 0; 
			// se itera el input buffer y se inserta en la salida
			while ((bufferLength = content.read(buffer)) > 0) {
				// se añade al file output
				fileOutput.write(buffer, 0, bufferLength);
			}
			// Se cierra el fileoutput
			fileOutput.close();
			// Se asigna a null para no volverlo a cerrar
			fileOutput=null;
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error guardando fichero");			
		} catch (IOException e) {
			Log.e(TAG, "Error guardando fichero");
		}finally{
			if(null != fileOutput)
			{
				// se cierra el output si no se ha cerrado
				try {
					fileOutput.close();
				} catch (IOException e) {					
					Log.e(TAG, "Error guardando fichero");
				}
			}
		}		
	}
	
	
		
	/**
	 * Comprueba si hay conexion a internet
	 * 
	 * @param contexto Contexto
	 * @return true si hay conexion
	 */
	public static boolean isNetworkAvailable(Context contexto) {
		Log.v(TAG, "INICIO isNetworkAvailable()");

		boolean resultado=false;
			
		ConnectivityManager connectivity = (ConnectivityManager) 
					contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.d(TAG, "Sin connectivity manager");
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						Log.v(TAG, "isNetworkAvailable() - Con red");
						resultado = true;
					}
				}
			}
		}
			
		Log.v(TAG, "FIN isNetworkAvailable()");
		return resultado;
	}
	
	/**
	 * Este metodo validara si el parametro recibido es numerico.
	 * @param cadena
	 * @return retornara true : si la cadena es un numero y false : si no es un numero
	 */
	public static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	
	
	
	
}
