package es.magicbox.hackathon.services;

import java.util.Random;

import es.magicbox.hackathon.utils.network.NetworkResponse;
import es.magicbox.hackathon.utils.network.NetworkUtilities;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


public class WebAPIService extends IntentService {

    private static final String TAG = WebAPIService.class.getSimpleName();


    public static String ACTION_QUERY_SENSOR = "es.magicbox.hackathon.services.WebAPIService.ACTION_QUERY_SENSOR";
    public static String ACTION_PARTY = "es.magicbox.hackathon.services.WebAPIService.ACTION_PARTY";

    public static String NOTIFICATION_SENSOR_UPDATE = "es.magicbox.hackathon.services.WebAPIService.NOTIFICATION_SENSOR_UPDATE";

    private String contextURI = "http://130.206.80.45:1029/ngsi10/queryContext";


    public WebAPIService() {
   		super("WebAPIService");
   	}

    @Override
    protected void onHandleIntent(Intent _intent) {
        if (_intent.getAction().equals(ACTION_QUERY_SENSOR)) {
            actionQuerySensor(_intent);
        } else if (_intent.getAction().equals(ACTION_PARTY)) {
        	partyTVLED(_intent);
        }
    }



    private void actionQuerySensor(Intent _intent) {

        String query1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "\t\t\t\t  <queryContextRequest>\n" +
                    "\t\t\t\t  \t<entityIdList>\n" +
                    "\t\t\t\t  \t\t<entityId type=\"Sensor\" isPattern=\"false\">\n" +
                    "\t\t\t\t  \t\t\t<id>4IN1:48:99:26:0002</id>\n" +
                    "\t\t\t\t  \t\t</entityId>\n" +
                    "\t\t\t\t  \t</entityIdList>\n" +
                    "\t\t\t\t  \t<attributeList>\n" +
                    "\t\t\t\t  \t\t<attribute>Move</attribute>\n" +
                    "\t\t\t\t  \t</attributeList>\n" +
                    "\t\t\t\t  </queryContextRequest>";
        String query2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "\t\t\t\t  <queryContextRequest>\n" +
                    "\t\t\t\t  \t<entityIdList>\n" +
                    "\t\t\t\t  \t\t<entityId type=\"Sensor\" isPattern=\"false\">\n" +
                    "\t\t\t\t  \t\t\t<id>4IN1:86:DA:B6:0002</id>\n" +
                    "\t\t\t\t  \t\t</entityId>\n" +
                    "\t\t\t\t  \t</entityIdList>\n" +
                    "\t\t\t\t  \t<attributeList>\n" +
                    "\t\t\t\t  \t\t<attribute>Move</attribute>\n" +
                    "\t\t\t\t  \t</attributeList>\n" +
                    "\t\t\t\t  </queryContextRequest>";
        String query3 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "\t\t\t\t  <queryContextRequest>\n" +
                    "\t\t\t\t  \t<entityIdList>\n" +
                    "\t\t\t\t  \t\t<entityId type=\"Sensor\" isPattern=\"false\">\n" +
                    "\t\t\t\t  \t\t\t<id>4IN1:B5:74:20:0002</id>\n" +
                    "\t\t\t\t  \t\t</entityId>\n" +
                    "\t\t\t\t  \t</entityIdList>\n" +
                    "\t\t\t\t  \t<attributeList>\n" +
                    "\t\t\t\t  \t\t<attribute>Move</attribute>\n" +
                    "\t\t\t\t  \t</attributeList>\n" +
                    "\t\t\t\t  </queryContextRequest>";
        String query4 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\t\t\t\t  <queryContextRequest>\n" +
                "\t\t\t\t  \t<entityIdList>\n" +
                "\t\t\t\t  \t\t<entityId type=\"Sensor\" isPattern=\"false\">\n" +
                "\t\t\t\t  \t\t\t<id>4IN1:48:99:26:0003</id>\n" +
                "\t\t\t\t  \t\t</entityId>\n" +
                "\t\t\t\t  \t</entityIdList>\n" +
                "\t\t\t\t  \t<attributeList>\n" +
                "\t\t\t\t  \t\t<attribute>Move</attribute>\n" +
                "\t\t\t\t  \t</attributeList>\n" +
                "\t\t\t\t  </queryContextRequest>";

        Log.d(TAG, "Start quering");
        NetworkResponse result1 = NetworkUtilities.postDataAuthResponse(contextURI, query1);
        NetworkResponse result2 = NetworkUtilities.postDataAuthResponse(contextURI, query2);
        NetworkResponse result3 = NetworkUtilities.postDataAuthResponse(contextURI, query3);
        NetworkResponse result4 = NetworkUtilities.postDataAuthResponse(contextURI, query4);


        Log.d(TAG, "Sensor activity: {zone1: " + result1.getContent().contains("QUIET") +
                ", zone2: " + result2.getContent().contains("QUIET") +
                ", zone3: " + result3.getContent().contains("QUIET") +
                ", zone4: " + result4.getContent().contains("QUIET") + "}");

        // Notify the update
        Intent broadcast = new Intent(NOTIFICATION_SENSOR_UPDATE);
        broadcast.putExtra("zone1", result1.getContent().contains("QUIET"));
        broadcast.putExtra("zone2", result2.getContent().contains("QUIET"));
        broadcast.putExtra("zone3", result3.getContent().contains("QUIET"));
        broadcast.putExtra("zone4", result4.getContent().contains("QUIET"));
        sendBroadcast(broadcast);
    }
    
    private void partyTVLED(Intent _intent) {
    	
    	String actuadorBase = "http://130.206.80.45:5371/m2m/v2/services/HACKSANTANDER/devices/";
    	String actuadorTV = "IR:48:99:26:0007/command";
    	String actuadorLED = "RGBS:48:99:26:0006/command";
    	
    	Random ran = new Random();
    	int R = ran.nextInt(99);
    	int G = ran.nextInt(99);
    	int B = ran.nextInt(99);
    	
    	String commandTV = "{\"commandML\":\"<paid:command name=\\\"SET\\\"><paid:cmdParam name=\\\"FreeText\\\"> <swe:Text><swe:value>FIZCOMMAND 0</swe:value></swe:Text></paid:cmdParam></paid:command>\"}";
    	String commandLED = "{\"commandML\":\"<paid:command name=\\\"SET\\\"><paid:cmdParam name=\\\"FreeText\\\"> <swe:Text><swe:value>FIZCOMMAND "+ R + "-" + G +"-" + B + "</swe:value></swe:Text></paid:cmdParam></paid:command>\"}";
    	
        Log.d(TAG, "Start LED command");
        NetworkUtilities.sendDataAuthResponse(
        		"POST", 
        		NetworkUtilities.JSON_MEDIA_TYPE, 
        		actuadorBase + actuadorLED, 
        		commandLED);

        
        Log.d(TAG, "Start TV command");
        NetworkUtilities.sendDataAuthResponse(
        		"POST", 
        		NetworkUtilities.JSON_MEDIA_TYPE, 
        		actuadorBase + actuadorTV, 
        		commandTV);
    }

}
