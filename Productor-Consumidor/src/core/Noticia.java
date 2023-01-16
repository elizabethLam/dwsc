package core;

/**
 * Mensaje para encapsulacion de datos
 * @author Elizabeth
 *
 */
public class Noticia {
	
	private String dateOfNews;
	private String shortDescription;
	private String longDescription;
	
	/**
	 * Constructor de un mensaje a partir de una fecha, una descripcion corta, y una descripcion larga
	 * @param dateOfNews cadena que representa a la fecha
	 * @param shortDescription cadena que representa la descripcion corta
	 * @param longDescription cadena que representa la descripcion larga
	 */
	public Noticia(String dateOfNews, String shortDescription, String longDescription) {
		this.dateOfNews = dateOfNews;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
	}
	
	/**
	 * Constructor copia
	 * @param nuevo mensaje fuente
	 */
	public Noticia(Noticia nuevo) {
		this.dateOfNews = nuevo.dateOfNews;
		this.shortDescription = nuevo.shortDescription;
		this.longDescription = nuevo.longDescription;
	}
		
	public String getDateOfNews() {
		return dateOfNews;
	}

	public void setDateOfNews(String dateOfNews) {
		this.dateOfNews = dateOfNews;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

}
