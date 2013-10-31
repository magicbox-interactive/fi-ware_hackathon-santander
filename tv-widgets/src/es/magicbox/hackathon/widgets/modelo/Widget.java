package es.magicbox.hackathon.widgets.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Modelo con el que poder manejar y gestionar la información asociada a un
 * Widget
 * 
 * 
 */
public class Widget {

	/**
	 * Nombre del widget
	 */
	private String nombre;

	/**
	 * Icono del widget en menu. Este parametro podria venir la ruta de mas de
	 * un icono separado por un PIPELINE "|".
	 */
	private String icono;

	/**
	 * Icono del widget en pestaña
	 */
	private String iconoTab;

	/**
	 * Posicion del widget
	 */
	private int posicion;

	/**
	 * Tipo de parametros que hay que pasar al widget
	 */
	private String params;

	/**
	 * Listado de vistas
	 */
	private List<Vista> vistas;

	/**
	 * Obtiene la url del icono para la visa del widget
	 * 
	 * @return url del icono de la pestaña
	 */
	public String getIconoTab() {
		return iconoTab;
	}

	/**
	 * Setea el icono para la vista del widget
	 * 
	 * @param iconoTab
	 *            Url del icono de la pestaña
	 */
	public void setIconoTab(String iconoTab) {
		this.iconoTab = iconoTab;
	}

	/**
	 * Obtiene el nombre del widget
	 * 
	 * @return nombre Nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Setea el nombre del widget
	 * 
	 * @param nombre
	 *            Nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Obtiene el icono del widget
	 * 
	 * @return icono Url del icono
	 */
	public String getIcono() {
		return icono;
	}

	/**
	 * Setea el icono del widget
	 * 
	 * @param icono
	 *            Url del icono
	 */
	public void setIcono(String icono) {
		this.icono = icono;
	}

	/**
	 * Metodo para obtener la posicion
	 * 
	 * @return posicion Posicion
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * Setea la posicion
	 * 
	 * @param posicion
	 *            Posicion
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	/**
	 * Método para obtener el listado con las vistas del widget
	 * 
	 * @return List<Vista> Listado de vistas
	 */
	public List<Vista> getVistas() {
		return vistas;
	}

	/**
	 * Añade los tipos de vistas al widget
	 * 
	 * @param vistas
	 *            Listado de vistas
	 */
	public void setVistas(List<Vista> vistas) {
		this.vistas = vistas;
	}

	/**
	 * Devuelve el tipo de parametros que necesita el widget
	 * 
	 * @return Tipo de parametros
	 */
	public String getParams() {
		return params;
	}

	/**
	 * Setea el tipo de parametros que utiliza el widget
	 * 
	 * @param params
	 *            - Parametros
	 */
	public void setParams(String params) {
		this.params = params;
	}

	/**
	 * Devolvera la lista de ruta de iconos , para el caso de widget como regla
	 * de negocio tendremos 2
	 * 
	 * @return
	 */
	public List<String> getIconValues() {
		List<String> strings = new ArrayList<String>();

		if (!getIcono().isEmpty() && getIcono().contains("|")) {
			StringTokenizer tokenizer = new StringTokenizer(getIcono(), "|");
			// para el caso de los xml solo vamos a contemplar 2 elementos
			strings.add(tokenizer.nextToken());
			strings.add(tokenizer.nextToken());

		} else {
			strings.add(getIcono());
			strings.add(getIcono());
		}

		return strings;
	}

}
