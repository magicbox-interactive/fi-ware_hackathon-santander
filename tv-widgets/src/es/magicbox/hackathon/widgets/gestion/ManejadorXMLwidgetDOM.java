package es.magicbox.hackathon.widgets.gestion;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.util.Log;

import es.magicbox.hackathon.widgets.modelo.Vista;
import es.magicbox.hackathon.widgets.modelo.Widget;

/**
 * Parseador de XML envivo
 * 
 */
public class ManejadorXMLwidgetDOM {
	/**
	 * TAG de los logos
	 */
	private static final String TAG = ManejadorXMLwidgetDOM.class.getSimpleName();

	/**
	 * Etiqueta LANG
	 */
	// private static final String LANG ="lang";
	/**
	 * Etiqueta version
	 */
	// private static final String VERSION ="version";
	/**
	 * Etiqueta widget
	 */
	private static final String WIDGET = "widget";
	/**
	 * Etiqueta name
	 */
	private static final String NAME = "name";
	/**
	 * Etiqueta icon
	 */
	private static final String ICON = "icon";
	/**
	 * Etiqueta icon
	 */
	private static final String ICON_TAB = "icon_tab";
	/**
	 * Etiqueta position
	 */
	private static final String POSITION = "position";
	/**
	 * Etiqueta vista
	 */
	private static final String VISTA = "vista";
	/**
	 * Etiqueta type
	 */
	private static final String TYPE = "type";
	/**
	 * Etiqueta params
	 */
	private static final String PARAMS = "params";

	/**
	 * Etiqueta webviewizquierda
	 */
	private static final String WEBVIEW_IZQUIERDO = "webviewizquierda";
	/**
	 * Etiqueta webviewderecha
	 */
	private static final String WEBVIEW_DERECHA = "webviewderecha";
	/**
	 * Etiqueta webviewcentro
	 */
	private static final String WEBVIEW_CENTRO = "webviewcentro";

	/**
	 * Etiqueta tv_width
	 */
	private static final String TV_WIDTH = "tv_width";

	/**
	 * Etiqueta tv_height
	 */
	private static final String TV_HEIGHT = "tv_height";

	/**
	 * Etiqueta tv_x
	 */
	private static final String TV_X = "tv_x";

	/**
	 * Etiqueta tv_y
	 */
	private static final String TV_Y = "tv_y";
	
	/**
	 * Etiqueta web_width
	 */
	private static final String WEB_WIDTH = "web_width";

	/**
	 * Etiqueta web_height
	 */
	private static final String WEB_HEIGHT = "web_height";

	/**
	 * Etiqueta web_x
	 */
	private static final String WEB_X = "web_x";

	/**
	 * Etiqueta web_y
	 */
	private static final String WEB_Y = "web_y";

	
	
	/**
	 * Etiqueta web_zoom
	 */
	private static final String WEB_ZOOM="web_zoom";
	

	/**
	 * Contexto de la activity
	 */
	private Context contexto;

	/**
	 * Constructor del parseador Menu con DOM
	 * 
	 * @param contexto
	 *            contexto de la activity
	 */
	public ManejadorXMLwidgetDOM(Context contexto) {
		Log.v(TAG, "Inicio ManejadorXMLwidgetDOM");
		this.contexto = contexto;
		Log.v(TAG, "Fin ManejadorXMLwidgetDOM");
	}

	/**
	 * Método con el que parsear el xml de widgets a través de DOM
	 * 
	 * @param urlWidgets
	 *            url del enlace
	 * 
	 * @return List<Widget> listado de widgets
	 * 
	 */
	public List<Widget> parseaWidget(String urlWidgets) {
		Log.v(TAG, "Inicio parseaWidget: " + urlWidgets);
		List<Widget> widgetsAux = new ArrayList<Widget>();

		try {
			// Llamamos al parseador de inputStream
			widgetsAux = parseaWidget(Utils.getRemoteStream(urlWidgets));
		} catch (Exception e) {
			Log.e(TAG, "Error a la hora de parsear el envivo " + urlWidgets);
			widgetsAux = null;
		}
		Log.v(TAG, "Fin parseaWidget: " + urlWidgets);
		return widgetsAux;
	}

	/**
	 * Método con el que parsear un envivo en un xml a través del inputstream
	 * 
	 * @param input
	 *            inputstream del fichero a parsear
	 * @return enVivo en vivo parseado
	 */
	public List<Widget> parseaWidget(InputStream input) {
		Log.v(TAG, "Inicio parseaWidget INPUT");

		List<Widget> listadoWidgets = new ArrayList<Widget>();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(input);

			// Obtenemos elmento ROOT: contenidos
			Element root = dom.getDocumentElement();
			// root.getAttribute(LANG));
			// root.getAttribute(VERSION));

			NodeList listadoHijos = root.getChildNodes();
			for (int i = 0; i < listadoHijos.getLength(); i++) {

				if (WIDGET.equals(listadoHijos.item(i).getNodeName())) {
					// Nuevo widget
					Widget widget = new Widget();

					Node nodeWidget = listadoHijos.item(i);
					if (nodeWidget.hasAttributes()) {
						widget.setNombre(nodeWidget.getAttributes().getNamedItem(NAME).getTextContent());
						widget.setIcono(nodeWidget.getAttributes().getNamedItem(ICON).getTextContent());
						widget.setIconoTab(nodeWidget.getAttributes().getNamedItem(ICON_TAB).getTextContent());
						widget.setPosicion(Integer.parseInt(nodeWidget.getAttributes().getNamedItem(POSITION).getTextContent()));
						Node params = nodeWidget.getAttributes().getNamedItem(PARAMS);
						if (null != params) {
							widget.setParams(params.getTextContent());
						}
					}

					if (nodeWidget.hasChildNodes()) {
						List<Vista> vistas = new ArrayList<Vista>();
						NodeList listadoVistas = nodeWidget.getChildNodes();
						for (int j = 0; j < listadoVistas.getLength(); j++) {
							if (VISTA.equals(listadoVistas.item(j).getNodeName())) {
								vistas.add(parsearVista(listadoVistas.item(j)));
							}
						}
						widget.setVistas(vistas);
					}

					listadoWidgets.add(widget);

				}
			}

		} catch (Exception e) {
			Log.e(TAG, "Error a la hora de parsear el envivo INPUT", e);
			listadoWidgets = null;
		}
		Log.v(TAG, "Fin parseaWidget INPUT");
		return listadoWidgets;
	}

	/**
	 * Parsea los elementos del tipo vista
	 * 
	 * @param nodeVista
	 *            nodo vista
	 * @return objeto Vista
	 */
	private Vista parsearVista(Node nodeVista) {
		// Crea una nueva vista
		Vista vista = new Vista();

		if (nodeVista.hasAttributes()) {
			// Setea el tipo de vista 1=vista sencilla / 2=vista en ele / 3=full
			// screen
			vista.setType(Integer.valueOf(nodeVista.getAttributes().getNamedItem(TYPE).getTextContent()));
		}

		// si la vista es de tipo full screen se recuperan los atributos
		// respectivos a esta vista que son posicion x , posicion y , ancho y
		// alto del video de la tv.

		if (vista.getType() == Vista.TYPE_FULL_SCREEN) {

			// validamos que exista los atributos en el tag vista

			if (nodeVista.getAttributes().getNamedItem(TV_X) != null) {
				if (Utils.isNumeric(nodeVista.getAttributes().getNamedItem(TV_X).getTextContent())) {
					vista.setPositionX(Integer.valueOf(nodeVista.getAttributes().getNamedItem(TV_X).getTextContent()));
				}
			}
			if (nodeVista.getAttributes().getNamedItem(TV_Y) != null) {
				if (Utils.isNumeric(nodeVista.getAttributes().getNamedItem(TV_Y).getTextContent())) {
					vista.setPositionY(Integer.valueOf(nodeVista.getAttributes().getNamedItem(TV_Y).getTextContent()));
				}
			}

			if (nodeVista.getAttributes().getNamedItem(TV_WIDTH) != null) {
				if (Utils.isNumeric(nodeVista.getAttributes().getNamedItem(TV_WIDTH).getTextContent())) {
					vista.setWidth(Integer.valueOf(nodeVista.getAttributes().getNamedItem(TV_WIDTH).getTextContent()));
				}
			}

			if (nodeVista.getAttributes().getNamedItem(TV_HEIGHT) != null) {
				if (Utils.isNumeric(nodeVista.getAttributes().getNamedItem(TV_HEIGHT).getTextContent())) {
					vista.setHeight(Integer.valueOf(nodeVista.getAttributes().getNamedItem(TV_HEIGHT).getTextContent()));
				}
			}
			
			/* tag de los parametros de configuracion de la vista para el webview  */
			if (nodeVista.getAttributes().getNamedItem(WEB_X) != null) {
				if (Utils.isNumeric(nodeVista.getAttributes().getNamedItem(WEB_X).getTextContent())) {
					vista.setWebX(Integer.valueOf(nodeVista.getAttributes().getNamedItem(WEB_X).getTextContent()));
				}
			}
			if (nodeVista.getAttributes().getNamedItem(WEB_Y) != null) {
				if (Utils.isNumeric(nodeVista.getAttributes().getNamedItem(WEB_Y).getTextContent())) {
					vista.setWebY(Integer.valueOf(nodeVista.getAttributes().getNamedItem(WEB_Y).getTextContent()));
				}
			}

			if (nodeVista.getAttributes().getNamedItem(WEB_WIDTH) != null) {
				if (Utils.isNumeric(nodeVista.getAttributes().getNamedItem(WEB_WIDTH).getTextContent())) {
					vista.setWebWidth(Integer.valueOf(nodeVista.getAttributes().getNamedItem(WEB_WIDTH).getTextContent()));
				}
			}

			if (nodeVista.getAttributes().getNamedItem(WEB_HEIGHT) != null) {
				if (Utils.isNumeric(nodeVista.getAttributes().getNamedItem(WEB_HEIGHT).getTextContent())) {
					vista.setWebHeight(Integer.valueOf(nodeVista.getAttributes().getNamedItem(WEB_HEIGHT).getTextContent()));
				}
			}
			
			if (nodeVista.getAttributes().getNamedItem(WEB_ZOOM) != null) {
				
					vista.setWebZoom(nodeVista.getAttributes().getNamedItem(WEB_ZOOM).getTextContent());
				
			}
			
		}

		if (nodeVista.hasChildNodes()) {
			NodeList listadoWebviews = nodeVista.getChildNodes();

			for (int j = 0; j < listadoWebviews.getLength(); j++) {
				if (WEBVIEW_IZQUIERDO.equals(listadoWebviews.item(j).getNodeName())) {
					// Seteo el webview de la izquierda
					vista.setWebviewizquierda(listadoWebviews.item(j).getTextContent());
				} else if (WEBVIEW_DERECHA.equals(listadoWebviews.item(j).getNodeName())) {
					// Seteo el webview de la derecha
					vista.setWebviewderecha(listadoWebviews.item(j).getTextContent());
				} else if (WEBVIEW_CENTRO.equals(listadoWebviews.item(j).getNodeName())) {
					// Seteo el webview del centro
					vista.setWebviewcentro(listadoWebviews.item(j).getTextContent());
				}

			}
		}

		return vista;
	}
}
