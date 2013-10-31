package es.magicbox.hackathon.widgets.async;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import es.magicbox.hackathon.widgets.gestion.Utils;

/**
 * Tarea asincrona que descarga una imagen a partir de una url
 * 
 *
 */
public class DownloadFileAsync  extends AsyncTask<String, Void, Bitmap>{

 	/**
   	 * TAG de logs
   	 */
   	private static final String TAG = DownloadFileAsync.class.getSimpleName();
   	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Bitmap doInBackground(String... params) {
		Log.v(TAG, "Inicio doInBackground de DownloadFileAsync");
		URL myFileUrl =null; 
		URLConnection conn = null;
		try {
			myFileUrl= new URL(params[0]);
			
			if (myFileUrl.getProtocol().equalsIgnoreCase("https")) {				
				conn = myFileUrl.openConnection();
				((HttpsURLConnection)conn).setHostnameVerifier(Utils.DO_NOT_VERIFY);
			}else{				
				conn= myFileUrl.openConnection();
			}
			conn.setDoInput(true);
			conn.setReadTimeout(Utils.CONNECTION_TIMEOUT_SECONDS);
			conn.connect();
			InputStream is = conn.getInputStream();
			
			Bitmap bmImg = BitmapFactory.decodeStream(is);
			Log.v(TAG, "Fin doInBackground de DownloadFileAsync");
			return bmImg;
		} catch (MalformedURLException e) {
			Log.e(TAG, "Error descargando imagen: MalformedURLException",e);
			return null;
		}catch (IOException e) {				
			Log.e(TAG, "Error descargando imagen: IOException",e);
			return null;
		}catch (Exception e) {
			Log.e(TAG, "Error descargando imagen" ,e);
			return null;
		}
	}	
	
	
	
}