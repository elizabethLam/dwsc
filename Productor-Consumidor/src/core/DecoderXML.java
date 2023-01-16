package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Decodificador de archivo XML (desempaquetador)
 * @author Elizabeth Garcia
 *
 */
public class DecoderXML {

	private File archivoXML;

	/**
	 * Constructor de objeto desempaquetador a partir de una ruta de archivo (nombre incluido) XML
	 * @param ruta_archivo cadena que representa la ruta del archivo (nombre incluido)
	 */
	public DecoderXML(String ruta_archivo) {
		this.archivoXML = new File(ruta_archivo);
	}

	/**
	 * Desempaquetar datos de archivo XML
	 * @return par de valores que contienen los mensajes del archivo XML y nos indica si el archivo ha sido desempaquetado correctamente
	 */
	public Pair<Boolean, ArrayList<Noticia>> desempaquetar() {
		ArrayList<Noticia> noticias = new ArrayList<Noticia>();
		Noticia nuevo = null;
		String dateOfNews = "";
		String shortDescription = "";
		String longDescription = "";
		try {
	           DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	           DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	           Document doc = dBuilder.parse(archivoXML);
	           doc.getDocumentElement().normalize();
	           //System.out.println("Elemento raiz :" + doc.getDocumentElement().getNodeName());
	           NodeList nList = doc.getElementsByTagName("noticia");
	           for (int temp = 0; temp < nList.getLength(); temp++) {
	             Node nNode = nList.item(temp);
	             //System.out.println("\nCurrent Elemento :" + nNode.getNodeName());
	             if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	                     Element eElement = (Element) nNode;
	                     //System.out.println("Atributo : " + eElement.getAttribute("id"));
	                     dateOfNews = eElement.getElementsByTagName("fecha").item(0).getTextContent();
	                     shortDescription = eElement.getElementsByTagName("descripcion_corta").item(0).getTextContent();
	                     longDescription = eElement.getElementsByTagName("descripcion_larga").item(0).getTextContent();
	                     nuevo = new Noticia(dateOfNews, shortDescription, longDescription);
	                     noticias.add(nuevo);
	              }
	          }
	          return new Pair<Boolean, ArrayList<Noticia>>(true, noticias);
		} catch (SAXParseException e) {
			System.err.println(e.getMessage());
		} catch (SAXException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.err.println(e.getMessage());
		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
		}
		return new Pair<Boolean, ArrayList<Noticia>>(false, null);
	}

	/**
	 * Obtener ruta del archivo XML
	 * @return cadena que representa la ruta del archivo
	 */
	public String getRutaArchivoXML() {
		return archivoXML.getAbsolutePath();
	}

	/**
	 * Obtener documento XML en forma de cadena
	 * @return cadena que representa el documento XML
	 */
	public String obtenerXMLComoString() {
		StringBuilder sb = new StringBuilder();
		BufferedReader br;
		String sCurrentLine;
		try {
			br = new BufferedReader(new FileReader(getRutaArchivoXML()));
			while ((sCurrentLine = br.readLine()) != null) {
				sb.append(sCurrentLine);
			}
			br.close();
		} catch (FileNotFoundException e1) {
			System.err.println(e1.getMessage());
		} catch (IOException e2) {
			System.err.println(e2.getMessage());
		}
		return sb.toString();
	}
}
