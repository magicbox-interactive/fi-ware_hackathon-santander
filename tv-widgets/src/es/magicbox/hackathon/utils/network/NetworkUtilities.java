package es.magicbox.hackathon.utils.network;

import android.util.Log;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class NetworkUtilities {
	
	private static final String TAG = "NetworkUtilities";

    public static final String JSON_MEDIA_TYPE = "application/json";
    public static final String XML_MEDIA_TYPE = "application/xml";
	
	/**
	 * Get using HttpURLConnection (HTTP and security)
	 * @param _url
	 * @return
	 */
	public static NetworkResponse getDataAuthResponse(String _url) {
		NetworkResponse result = null;
		HttpURLConnection urlConnection = null;
		HttpURLConnection.setFollowRedirects(true);

		try {			
			// Create the connection
			urlConnection = createHttpURLConnection(_url);
									
			urlConnection.addRequestProperty("Accept", XML_MEDIA_TYPE);
			
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			
			// Transform content to String
			String content = readInputStream(in);
			
			// Close and disconnect
			in.close();			
			urlConnection.disconnect();
			
			result = new NetworkResponse(urlConnection.getResponseCode(),
									     urlConnection.getResponseMessage(),
									     content);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "getDataAuthResponse", e);
			result = new NetworkResponse(404, "Not Found", null);
		} catch (IOException e) {
			Log.e(TAG, "getDataAuthResponse", e);
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "getDataAuthResponse", e);
		} catch (KeyManagementException e) {
			Log.e(TAG, "getDataAuthResponse", e);
		} catch (KeyStoreException e) {
			Log.e(TAG, "getDataAuthResponse", e);
		}	
		finally {
			if (urlConnection != null) urlConnection.disconnect();
		}
		return result;
	}
   
	/**
	 * PUT the JSON information into the _url location, with authentication
	 * @param _user
	 * @param _password
	 * @param _url
	 * @param _jsonData
	 * @return
	 */
	public static NetworkResponse putDataAuthResponse(String _url,
											   	      String _data) {

        return sendDataAuthResponse("PUT", XML_MEDIA_TYPE, _url, _data);
		
	}


    /**
   	 * POST the JSON information into the _url location, with authentication
   	 * @param _user
   	 * @param _password
   	 * @param _url
   	 * @param _jsonData
   	 * @return
   	 */
   	public static NetworkResponse postDataAuthResponse(String _url,
   											   	       String _data) {

           return sendDataAuthResponse("POST", XML_MEDIA_TYPE, _url, _data);

   	}

    /**
   	 * Send the data information into the _url location, with authentication.
     * HTTP method and _content_type as a parameter.
     * @param _method HTTP method (POST or PUT)
     * @param _content_type (application/json text/xml ...)
   	 * @param _url
   	 * @param _data
   	 * @return
   	 */
   	public static NetworkResponse sendDataAuthResponse(String _method,
                                                        String _content_type,
   											   	        String _url,
   											   	        String _data) {
   		NetworkResponse result = null;
   		HttpURLConnection urlConnection = null;


   		try {
   			// Create the connection
   			urlConnection = createHttpURLConnection(_url);


   			urlConnection.addRequestProperty("Content-Type", _content_type);

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(_method);
            urlConnection.setUseCaches(false);

   			OutputStreamWriter out = new OutputStreamWriter(
   					urlConnection.getOutputStream());

   			out.write(_data);
   			out.close();

   			InputStream in = new BufferedInputStream(urlConnection.getInputStream());

   			// Transform content to String
   			String content = readInputStream(in);

   			// Close and disconnect
   			in.close();
   			urlConnection.disconnect();

   			result = new NetworkResponse(urlConnection.getResponseCode(),
   									     urlConnection.getResponseMessage(),
   									     content);
   		} catch (FileNotFoundException e) {
   			Log.e(TAG, "sendDataAuthResponse - " + _method, e);
   			result = new NetworkResponse(404, "Not Found", null);
   		} catch (IOException e) {
   			Log.e(TAG, "sendDataAuthResponse - " + _method, e);
   		}
        catch (NoSuchAlgorithmException e) {
   			Log.e(TAG, "sendDataAuthResponse - " + _method, e);
   		} catch (KeyManagementException e) {
   			Log.e(TAG, "sendDataAuthResponse - " + _method, e);
   		} catch (KeyStoreException e) {
   			Log.e(TAG, "sendDataAuthResponse - " + _method, e);
   		}
   		finally {
   			if (urlConnection != null) urlConnection.disconnect();
   		}
   		return result;

   	}

	
	/**
	 * Aux method to read InputStream and transform to String
	 * @param _in
	 * @return
	 * @throws java.io.IOException
	 */
	private static String readInputStream(InputStream _in) throws IOException {
		
		// Read dynamic
		ArrayList<Byte> bContent = new ArrayList<Byte>();
		int b = _in.read();
		while (b != -1) {
			bContent.add((byte) b);
			b = _in.read();
		}

		// Transform to byte[]
		byte[] sContent = new byte[bContent.size()];
		for (int i = 0; i < bContent.size(); i++) {
			sContent[i] = bContent.get(i);
		}

		// Transform content to String
		return new String(sContent);
	}

	/**
	 * Aux method to create HttpURLConnection: easy for HTTP, but 
	 * HTTPS need special config.
	 * @param _url
	 * @return
	 * @throws java.security.KeyStoreException
	 * @throws java.security.NoSuchAlgorithmException
	 * @throws java.security.KeyManagementException
	 * @throws java.io.IOException
	 */
	private static HttpURLConnection createHttpURLConnection(String _url) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException {
		
		HttpURLConnection urlConnection = null;
		URL url = new URL(_url);

		if (_url.toLowerCase().startsWith("https")) {
			// FOR SSL (HTTPS) -> Implement certificate verification ...
//			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//			TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
//			tmf.init(keyStore);
//			
//			SSLContext context = SSLContext.getInstance("TLS");
//			context.init(null, tmf.getTrustManagers(), null);

			urlConnection = (HttpsURLConnection) url.openConnection();				
			//((HttpsURLConnection)urlConnection).setSSLSocketFactory(context.getSocketFactory());
		} else {
			// Just HTTP
			urlConnection = (HttpURLConnection) url.openConnection();
		}
		
		return urlConnection;		
	}	
}
