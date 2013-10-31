package com.amlogic.dvb;

import android.app.Activity;
import android.content.Context;

/**
 * Proprietary software, can't distribute source code details
 *
 */
public class DVBClient extends Activity {


	public DVBClient(Context ctx){	}
	
	public int connect() { return 0; }
	

	public interface OnEventListener {
		public void onEvent(DVBClient c, DVBEvent e);

		public void onConnected(DVBClient c);

		public void onDisconect(DVBClient dvbClient);
	}

	public void setEventListener(OnEventListener l) { }

	public boolean isPlaying() {
		return false;
	}

	public void stopPlaying() {	}

	public Object getCurrentServiceID() {
		return null;
	}

	public void playByServiceID(Object currentServiceID) {
				
	}

	public boolean isConnected() {
		return true;
	}

	public void disconnect() {
	}

	
}
