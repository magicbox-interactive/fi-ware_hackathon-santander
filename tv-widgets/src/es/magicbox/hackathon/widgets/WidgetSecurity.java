package es.magicbox.hackathon.widgets;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

import es.magicbox.hackathon.widgets.R;

import es.magicbox.hackathon.services.WebAPIService;

public class WidgetSecurity extends Activity {
	
    private static final String TAG = WidgetSecurity.class.getSimpleName();

	ImageButton _button1;
	ImageButton _button2;
	ImageButton _button3;
	ImageButton _button4;
    
    private Timer mTimer = null;
    
    ImageView imageView = null;

    private SensorUpdateReceiver sensorUpdateReceiver = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_widget);
		
		_button1 = (ImageButton) this.findViewById(R.id.buttonCam1);
		_button1.setOnClickListener(listenerButton1Click);
		_button2 = (ImageButton) this.findViewById(R.id.buttonCam2);
		_button2.setOnClickListener(listenerButton2Click); 
		_button3 = (ImageButton) this.findViewById(R.id.buttonCam3);
		_button3.setOnClickListener(listenerButton3Click);	
		_button4 = (ImageButton) this.findViewById(R.id.buttonCam4);
		_button4.setOnClickListener(listenerButton4Click);	

		imageView = (ImageView) this.findViewById(R.id.imageViewVitamio);

		
        Log.d(TAG, "Update receiver registering ...");

		// Register the update receiver
        sensorUpdateReceiver = new SensorUpdateReceiver();
		IntentFilter intentFilter = new IntentFilter(WebAPIService.NOTIFICATION_SENSOR_UPDATE);
		this.registerReceiver(sensorUpdateReceiver, intentFilter);		
		
        Log.d(TAG, "Timer starting ...");
        // Create service for query
		mTimer = new Timer();
		mTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Intent sensorQuery = new Intent(WidgetSecurity.this, WebAPIService.class);
				sensorQuery.setAction(WebAPIService.ACTION_QUERY_SENSOR);
				startService(sensorQuery);
			}
		}, 1000, 10000);
		
		// listener for fi-ware logo
		ImageView logo_fiware = (ImageView) this.findViewById(R.id.imageViewLogoFiWare);
		logo_fiware.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent partyIntent = new Intent(WidgetSecurity.this, WebAPIService.class);
				partyIntent.setAction(WebAPIService.ACTION_PARTY);
				startService(partyIntent);	
			}
			
		});
	}
	
	/**
	 * Cambia el color del boton (rojo / verde)
	 * @param buttonNumber Numero del boton (1, 2 o 3)
	 * @param state Estado (true = verde / false = rojo)
	 */
	private void setButtonState(int buttonNumber, boolean state)
	{
		switch(buttonNumber)
		{
		case 1:
			if (state)
				_button1.setImageDrawable(getResources().getDrawable(R.drawable.green_light));
			else
				_button1.setImageDrawable(getResources().getDrawable(R.drawable.red_light));				
			break;
		case 2:
			if (state)
				_button2.setImageDrawable(getResources().getDrawable(R.drawable.green_light));
			else
				_button2.setImageDrawable(getResources().getDrawable(R.drawable.red_light));				
			break;
		case 3:
			if (state)
				_button3.setImageDrawable(getResources().getDrawable(R.drawable.green_light));
			else
				_button3.setImageDrawable(getResources().getDrawable(R.drawable.red_light));				
			break;
		case 4:
			if (state)
				_button4.setImageDrawable(getResources().getDrawable(R.drawable.green_light));
			else
				_button4.setImageDrawable(getResources().getDrawable(R.drawable.red_light));				
			break;
		}
	}

	private OnClickListener listenerButton1Click = new OnClickListener()
	{
		public void onClick(View v)
		{
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.gif1));
//			videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath() + "/zona1.3gp");
//			videoView.start();
		}
	};
	private OnClickListener listenerButton2Click = new OnClickListener()
	{
		public void onClick(View v)
		{
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.gif2));
//			videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath() + "/zona2.mp4");
//			videoView.start();
		}
	};
	private OnClickListener listenerButton3Click = new OnClickListener()
	{
		public void onClick(View v)
		{
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.gif3));
//			videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath() + "/zona3.ts");
//			videoView.start();
		}
	};
	private OnClickListener listenerButton4Click = new OnClickListener()
	{
		public void onClick(View v)
		{
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.gif4));
//			videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath() + "/zona4.ts");
//			videoView.start();
		}
	};
	
	
	
	
	@Override
	protected void onPause() {
		super.onPause();
		mTimer.cancel();
        this.unregisterReceiver(sensorUpdateReceiver);

	}




	private class SensorUpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(WebAPIService.NOTIFICATION_SENSOR_UPDATE)) {
				setButtonState(1, intent.getBooleanExtra("zone1", true));
				setButtonState(2, intent.getBooleanExtra("zone2", true));
				setButtonState(3, intent.getBooleanExtra("zone3", true));
				setButtonState(4, intent.getBooleanExtra("zone4", true));
			}
		}

	}

}

