package es.magicbox.hackathon.widgets.modelo;

/**
 * Modelo con el que poder manejar y gestionar la información asociada a una
 * Vista del widget
 * 
 */
public class Vista {

	/**
	 * Tipo de vista simple para los widgets
	 */
	public static final int TYPE_SIMPLE = 1;

	/**
	 * Tipo de vista en ele para los widgets
	 */
	public static final int TYPE_ELE = 2;

	/**
	 * Tipo de vista full screen para los widgets
	 */
	public static final int TYPE_FULL_SCREEN = 3;

	/**
	 * Indica el tipo de vista
	 */
	private int type;

	/**
	 * Indica la fuente html del webview de la izqueirda
	 */
	private String webviewizquierda;

	/**
	 * Indica la fuente html del webview de la derecha
	 */
	private String webviewderecha;

	/**
	 * Indica la fuente html del webview de la centro
	 */
	private String webviewcentro;
	
	
	/**
	 * Ancho de la TV
	 */
	private int width=0;

	/**
	 * Alto de la TV
	 */
	private int height=0;

	/**
	 * Posicion inicial en el eje x
	 * Se le inicializa con el -1 porque este valor nunca podra ser un valor valido para este atributo 
	 */
	private int positionX=-1;

	/**
	 * Posicion inicial en el eje Y
	 * Se le inicializa con el -1 porque este valor nunca podra ser un valor valido para este atributo 
	 */
	private int positionY=-1;
	

	/**
	 * Ancho de la TV
	 */
	private int webWidth=0;

	/**
	 * Alto de la TV
	 */
	private int webHeight=0;

	/**
	 * Posicion inicial en el eje x
	 * Se le inicializa con el -1 porque este valor nunca podra ser un valor valido para este atributo 
	 */
	private int webX=-1;

	/**
	 * Posicion inicial en el eje Y
	 * Se le inicializa con el -1 porque este valor nunca podra ser un valor valido para este atributo 
	 */
	private int webY=-1;
	
	/**
	 * El tipo de zoom que se aplicara al web view que se muestra en modo full screem , los valores podrian ser
	 * FAR (alejar), MEDIUM (normal) , CLOSE (acercar) 
	 */
	private String webZoom;
	
	

	/**
	 * Obtiene el tipo de vista
	 * 
	 * @return 1 vista simple, 2 vista ele , 3 vista full screen
	 */

	public int getType() {
		return type;
	}

	/**
	 * Setea el tipo de vsta
	 * 
	 * @param type
	 *            Tipo de vista
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Devuelve la url a cargar en el webview de la izquierda
	 * 
	 * @return Url webview izquierda
	 */
	public String getWebviewizquierda() {
		return webviewizquierda;
	}

	/**
	 * Setea la url del webview de la izquierda
	 * 
	 * @param webviewizquierda
	 *            Url del webview
	 */
	public void setWebviewizquierda(String webviewizquierda) {
		this.webviewizquierda = webviewizquierda;
	}

	/**
	 * Obtiene la url del webview de la derecha
	 * 
	 * @return Url webview
	 */
	public String getWebviewderecha() {
		return webviewderecha;
	}

	/**
	 * Setea la url del webview de la derecha
	 * 
	 * @param webviewderecha
	 *            Url del webview
	 */
	public void setWebviewderecha(String webviewderecha) {
		this.webviewderecha = webviewderecha;
	}

	/**
	 * Obtiene la url del webview del centro
	 * 
	 * @return Url del webview
	 */
	public String getWebviewcentro() {
		return webviewcentro;
	}

	/**
	 * Setea la url del webview del centro
	 * 
	 * @param webviewcentro
	 *            Url del webview
	 */
	public void setWebviewcentro(String webviewcentro) {
		this.webviewcentro = webviewcentro;
	}

	/**
	 * Obtiene el ancho que tendra el video de la tv
	 * 
	 * @return width ancho de la tv
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Setea el ancho que tendra el video de la tv
	 * 
	 * @param width
	 *            ancho de la tv
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Obtiene el alto que tendra el video de la tv
	 * 
	 * @return height alto de la tv
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Setea el alto que tendra el video de la tv
	 * 
	 * @param height
	 *            alto de la tv
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Obtiene la posicion de la esquina superior izquierda del eje X de la tv
	 * 
	 * @return positionX posicion del eje X del video de la tv
	 */
	public int getPositionX() {
		return positionX;
	}

	/**
	 * Setea la posicion de la esquina superior izquierda del eje X de la tv
	 * 
	 * @param positionX
	 *            posicion del eje X del video de la tv
	 */
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	/**
	 * Obtiene la posicion de la esquina superior izquierda del eje Y de la tv
	 * 
	 * @return positionY posicion del eje Y del video de la tv
	 */
	public int getPositionY() {
		return positionY;
	}

	/**
	 * Setea la posicion de la esquina superio izquierda del eje Y de la tv
	 * 
	 * @param positionY
	 *            posicion del eje Y del video de la tv
	 */
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	



	/**
	 * Este metodo evalua si los valores de posicion y dimension de la tv que son :  width, height , positionX ,positionY sean diferentes de cero. 
	 * @return true | false 
	 */
	public Boolean isValidParamTv() {

		Boolean exist = Boolean.FALSE;

		if (positionX > 0 && positionY > 0 && width > 0 && height > 0) {

			exist = Boolean.TRUE;
		}

		return exist;
	}
	
	/**
	 * Este metodo evalua si los valores de posicion y dimension de la vista que se aplicaran al WebView que son :  webWidth, webHeight , webX ,webY sean diferentes de cero. 
	 * @return true | false 
	 */
	public Boolean isValidParamWeb() {

		Boolean exist = Boolean.FALSE;

		if (webX > 0 && webY > 0 && webWidth > 0 && webHeight > 0) {

			exist = Boolean.TRUE;
		}

		return exist;
	}

	public int getWebWidth() {
		return webWidth;
	}

	public void setWebWidth(int webWidth) {
		this.webWidth = webWidth;
	}

	public int getWebHeight() {
		return webHeight;
	}

	public void setWebHeight(int webHeight) {
		this.webHeight = webHeight;
	}

	public int getWebX() {
		return webX;
	}

	public void setWebX(int webX) {
		this.webX = webX;
	}

	public int getWebY() {
		return webY;
	}

	public void setWebY(int webY) {
		this.webY = webY;
	}

	public String getWebZoom() {
		return webZoom;
	}

	public void setWebZoom(String webZoom) {
		this.webZoom = webZoom;
	}
	
	

}
