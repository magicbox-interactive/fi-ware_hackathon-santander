package es.magicbox.hackathon.widgets.exception;

public class WidgetException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Constructor
	 * 
	 * @param mensaje mensaje de error
	 */
	public WidgetException(String mensaje){
		super(mensaje);
	}
	
	
	/**
	 * Constructor 
	 * 
	 * @param mensaje mensaje de error 
	 * @param e excepción original
	 */
	public WidgetException(String mensaje, Throwable e){
		super(mensaje, e);
	}
	
	

}
