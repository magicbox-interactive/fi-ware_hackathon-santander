package es.magicbox.hackathon.widgets.adapter;

import android.graphics.Bitmap;

public class IconoWidget {

	private Bitmap bitmap;
	private String nombre;
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
