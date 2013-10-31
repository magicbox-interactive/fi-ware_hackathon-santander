package es.magicbox.hackathon.widgets;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import es.magicbox.hackathon.widgets.R;

import es.magicbox.hackathon.background.display.manager.BackgroudDisplayManager;

public class JSInterface {

	private WebView mAppView;
	private Context contexto;

	public JSInterface (WebView appView, Context contexto) {
		this.mAppView = appView;
		this.contexto = contexto;
	}

	public void doEchoTest(String echo){
		Toast toast = Toast.makeText(mAppView.getContext(), echo, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public void resizeTv(int x, int y, int width, int height){
		Log.e("Widgets", "##### RESIZE TV");
		try{
			BackgroudDisplayManager.setVideoWindow(x, y, width, height);
		}catch (Exception e) {
			Log.e("Widgets", "Problema al redimensionar la televisión");
		}
	}
	
	public void resizeWebview(final int x, final int y, final int width, final int height){
		Log.e("Widgets", "##### RESIZE WB");
		((Activity) mAppView.getContext()).runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						try{
							RelativeLayout relLayout = (RelativeLayout) ((Activity) mAppView.getContext())
									.findViewById(R.id.webFullScreem);		
							RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
									RelativeLayout.LayoutParams.WRAP_CONTENT,
									RelativeLayout.LayoutParams.WRAP_CONTENT);
							params.leftMargin = x;
							params.topMargin = y;
							params.height = height;
							params.width = width;
							relLayout.setLayoutParams(params);
						}catch (Exception e) {
							Log.e("Widgets", "Problema al redimensionar el webview");
						}
					}
				});
		
	}

}
