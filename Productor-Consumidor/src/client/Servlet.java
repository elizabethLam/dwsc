package client;

import java.io.File;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.ORB;
import org.omg.CORBA.StringHolder;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import com.google.gson.Gson;

import core.DecoderXML;
import core.CoderXML;
import core.Noticia;
import core.Pair;
import core.Parser;
import core.Validador;
import BufferApp.Buffer;
import BufferApp.BufferHelper;

/**
 * Capa cliente (Servlet)
 * 
 * @author Elizabeth
 *
 */
@WebServlet("/servlet")
public class Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String[] args = { "-ORBInitialPort", "900", "ORBInitialHost", "localhost" };
	private ORB orb;
	private org.omg.CORBA.Object objRef;
	private NamingContextExt ncRef;
	private String name;
	private Buffer bufferImpl;
	private StringHolder elem;
	private StringHolder bufferLength = new StringHolder();
	private CoderXML empaquetador;
	private DecoderXML desempaquetador;
	private Validador validador;
	private String nombre_archivo;
	private String ruta_archivo;
	private Boolean firstRead;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Servlet() {
		super();
		try {
			this.orb = ORB.init(args, null);
			this.objRef = orb.resolve_initial_references("NameService");
			this.ncRef = NamingContextExtHelper.narrow(objRef);
			this.name = "Buffer";
			this.bufferImpl = BufferHelper.narrow(ncRef.resolve_str(name));
		} catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace(System.out);
		}
		this.nombre_archivo = "documentoProductorConsumidor";
		this.ruta_archivo = System.getProperty("user.home") + File.separator + "productor_consumidor_files"
				+ File.separator + nombre_archivo + ".xml";
		this.firstRead = false;
	}

	/**
	 * Sobrecargamos el metodo get para que muestre un mensaje de error indicando
	 * que los parametros no pueden ser enviados al servlet por este metodo
	 * 
	 * @param request  contiene la peticion del usuario sobre la servlet
	 * @param response representa la respuesta de la servlet a la peticion
	 */
	/*
	 * protected void doGet(HttpServletRequest request, HttpServletResponse
	 * response) throws ServletException, IOException {
	 * response.setContentType("text/html"); PrintWriter out = response.getWriter();
	 * out.println("<html>"); out.println("<head>"); out.println("</head>");
	 * out.println("<body>");
	 * out.println("Error! Los parametros no han sido enviados por el metodo POST");
	 * out.println("</body>"); out.println("</html>"); }
	 */

	/*
	 * Función que valida el formato de la fecha dd/mm/yyyy
	 */
	public static boolean validateDateOfNews(String pDate) {
		try {
			SimpleDateFormat pDateFormat = new SimpleDateFormat("dd/mm/yyyy");
			pDateFormat.parse(pDate);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/*
	 * Función que valida el si un string es un numero
	 */
	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			int d = Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * Este metodo es llamado cuando la informacion le hes enviada a la servlet por
	 * el metodo POST a traves del protocolo HTTP. Imprime el formulario a traves
	 * del cual el usuario podra enviar datos y por el cual se le mostrara la
	 * informacion al usuario.
	 * 
	 * @param request  contiene la peticion del usuario sobre la servlet
	 * @param response representa la respuesta de la servlet a la peticion actual.
	 */
	/*
	 * protected void doPost(HttpServletRequest request, HttpServletResponse
	 * response) throws ServletException, IOException {
	 * response.setContentType("text/html");
	 * 
	 * PrintWriter out = response.getWriter(); out.println("<html>");
	 * out.println("<head>"); out.
	 * println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">"
	 * ); out.println("<meta name=\"Author\" content=\"Elizabeth Garcia Medina\">");
	 * out.
	 * println("<meta name=\"Description\" content=\"Universidad de Almeria (España)\">"
	 * ); out.println("<title>Productor-Consumidor</title>");
	 * out.println("</head>"); out.
	 * println("<body style=\"color: rgb(0, 0, 0); background-color: rgb(255, 255, 255);\" alink=\"#990000\" link=\"#043a66\" vlink=\"#999900\">"
	 * ); out.println("<p>"); out.
	 * println("<b> <font face=\"Arial,Helvetica\" color=\"#003366\" size=\"+1\">Gestor de noticias (Productor-Consumidor)</font></b>"
	 * ); out.println("</p>"); out.println("<table><tr><td>"); out.
	 * println("<form action=\"http://localhost:8080/Productor-Consumidor/Servlet\" method=\"post\">"
	 * ); out.println("<font face=\"Arial,Helvetica\" size=\"-1\">Fecha:</font>");
	 * out.println("<input type=\"date\" name=\"fecha\" size=\"20\">");
	 * out.println("<br><br>"); out.
	 * println("<font face=\"Arial,Helvetica\" size=\"-1\">Descripción corta:</font>"
	 * ); out.println("<p><input name=\"descripcion_corta\" size=\"45\"></p>"); out.
	 * println("<font face=\"Arial,Helvetica\" size=\"-1\">Descripción larga:</font>"
	 * ); out.
	 * println("<p><textarea name=\"descripcion_larga\" rows=\"10\" cols=\"40\"></textarea></p>"
	 * ); out.
	 * println("<input name=\"action\" value=\"Enviar\" alt=\"Pulsar boton para enviar elemento al buffer\" type=\"submit\"/>"
	 * ); out.
	 * println("<input value=\"Reset\" alt=\"Pulsar boton para reset info.\" type=\"reset\"/>"
	 * ); out.println("|"); out.
	 * println("<input name=\"action\" value=\"Recibir\" alt=\"Pulsar boton para recibir ultimo elemento del buffer\" type=\"submit\">"
	 * ); out.
	 * println("<input name=\"action\" value=\"Leer\" alt=\"Pulsar boton para leer ultimo elemento del buffer\" type=\"submit\"> "
	 * ); out.println("|"); out.
	 * println("<input name=\"action\" value=\"Ver buffer\" alt=\"Pulsar boton para ver el contenido del buffer\" type=\"submit\">"
	 * ); out.println("</form>"); out.println("</td>");
	 * out.println("<td>&nbsp;&nbsp;&nbsp;</td>");
	 * out.println("<td style=\"vertical-align: top;\">"); out.
	 * println("<form action=\"http://localhost:8080/Productor-Consumidor/Servlet\" method=\"post\">"
	 * ); out.
	 * println("<font face=\"Arial,Helvetica\" size=\"-1\">Longitud del buffer:</font>"
	 * ); out.println("<input name=\"pBufferLength\" size=\"5\">");
	 * out.println("<br><br>"); out.
	 * println("<input name=\"action\" value=\"Longitud buffer\" alt=\"Pulsar boton para fijar el numero maximo de elementos permitidos en el buffer\" type=\"submit\"/>"
	 * ); out.
	 * println("<input name=\"action\" value=\"Get longitud\" alt=\"Pulsar boton para obtener la longitud buffer\" type=\"submit\"/>"
	 * ); out.println("</form>");
	 * 
	 * if (request.getParameter("action").compareTo("Enviar") == 0) { String
	 * dateOfNews = request.getParameter("fecha").toString(); String
	 * shortDescription = request.getParameter("descripcion_corta").toString();
	 * String longDescription =
	 * request.getParameter("descripcion_larga").toString();
	 * 
	 * if(dateOfNews.compareTo("") == 0) { out.
	 * println("<font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: La fecha no puede estar vacía.</font>"
	 * ); } else if(!dateOfNews.matches(
	 * "(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}")) { out.
	 * println("<font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: La fecha no tiene el formato válido dd/mm/aaaa.</font>"
	 * ); } else if (shortDescription.compareTo("") == 0 ||
	 * shortDescription.replace("/\s+/g", "").length() < 5) { out.
	 * println("<font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: La descripción corta de la noticia no puede estar vacía, ni puede tener menos de 5 caracteres.</font></font></font>"
	 * ); } else if (longDescription.compareTo("") == 0 ||
	 * longDescription.replace("/\s+/g", "").length() < 20) { out.
	 * println("<font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: La descripción larga de la noticia no puede estar vacía, ni puede tener menos de 20 caracteres.</font></font></font>"
	 * ); } else if(shortDescription.replace("/\s+/g", "").length() > 30) { out.
	 * println("<font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: La descripción corta no puede tener más de 30 caracteres.</font>"
	 * ); } else if(longDescription.replace("/\s+/g", "").length() > 500) { out.
	 * println("<font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: La descripción larga no puede tener más de 500 caracteres.</font>"
	 * ); } else { ArrayList<String> datesOfNews = new ArrayList<String>(); // lista
	 * de fechas ArrayList<String> shortDescriptions = new ArrayList<String>(); //
	 * lista de descripciones cortas ArrayList<String> longDescriptions = new
	 * ArrayList<String>(); // lista de descripciones largas
	 * datesOfNews.add(request.getParameter("fecha")); //para obtener valor de la
	 * fecha shortDescriptions.add(request.getParameter("descripcion_corta"));
	 * //para obtener valor de la descripcion corta
	 * longDescriptions.add(request.getParameter("descripcion_larga")); //para
	 * obtener valor de la descripcion larga empaquetador = new
	 * CoderXML(nombre_archivo); //crear archivo XML (empaquetar)
	 * if(empaquetador.empaquetar(datesOfNews, shortDescriptions,
	 * longDescriptions)){ out.
	 * println("<font face=\"Arial,Helvetica\" color=\"green\" size=\"-1\">[OK]: El documento XML se ha creado correctamente.</font>"
	 * ); String xsd =
	 * System.getProperty("user.home")+File.separator+"productor_consumidor_files"+
	 * File.separator+"documentoProductorConsumidor.xsd"; Parser parser = new
	 * Parser(ruta_archivo); //validar sintacticamente el mensaje XML (antes de ser
	 * enviado) if(parser.validar()){ out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"green\" size=\"-1\">[OK]: El documento XML ha sido validado sintácticamente de forma correcta.</font>"
	 * ); File xsd_file = new File(xsd); if (!xsd_file.exists()){ out.
	 * println("<br>font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: No se ha encontrado el esquema XSD para validar mensajes XML de Productor-Consumidor.</font>"
	 * ); out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: El documento XML no ha sido almacenado en el buffer del servidor.</font>"
	 * ); }else{ validador = new Validador(empaquetador.getRutaArchivoXML(), xsd);
	 * //validar archivo XML semanticamente (antes de ser enviado)
	 * if(validador.validar()){ out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"green\" size=\"-1\">[OK]: El documento XML ha sido validado semanticamente de forma correcta.</font>"
	 * ); String mensaje = new String(empaquetador.obtenerXMLComoString()); //enviar
	 * archivo XML en forma de String if(bufferImpl.put(mensaje)){ firstRead = true;
	 * out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"green\" size=\"-1\">[OK]: El documento XML ha sido almacenado correctamente en el buffer del servidor.</font>"
	 * ); }else{ out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: El documento XML no ha sido almacenado en el buffer del servidor [Pila llena].</font>"
	 * ); } }else{ out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: El documento XML no es valido.</font>"
	 * ); out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: El documento XML no ha sido almacenado en el buffer del servidor.</font>"
	 * ); } } } else { out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: Documento XML no valido sintacticamente.</font>"
	 * ); } }else{ out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: No se ha podido almacenar el documento XML.</font>"
	 * ); } } } else if (request.getParameter("action").compareTo("Recibir") == 0) {
	 * elem = new StringHolder(); //obtener XML del servidor en forma de String
	 * if(bufferImpl.get(elem)){ out.
	 * println("<font face=\"Arial,Helvetica\" size=\"-1\">Recibiendo elemento.</font>"
	 * ); empaquetador = new CoderXML(nombre_archivo); //crear arcivo XML recibido a
	 * partir del String if(empaquetador.empaquetar(elem.value)){ desempaquetador =
	 * new DecoderXML(empaquetador.getRutaArchivoXML()); //desempaquetar datos del
	 * archivo XML Pair<Boolean, ArrayList<Mensaje>> nuevo = new Pair<Boolean,
	 * ArrayList<Mensaje>>(desempaquetador.desempaquetar()); if(nuevo.first()){ for
	 * (Mensaje mensaje : nuevo.second()) { out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">Mensaje recibido:</font>"
	 * ); out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">Fecha: "
	 * +mensaje.getDateOfNews()+"</font>"); out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">Descripción corta: "
	 * +mensaje.getShortDescription()+"</font>"); out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">Descripción larga: "
	 * +mensaje.getLongDescription()+"</font>"); out.println("<br>"); } }else{ out.
	 * println("<br><font face=\"Arial,Helvetica\"> color=\"red\" size=\"-1\">[ERROR]: El mensaje no se ha desempaquetado correctamente.</font>"
	 * ); } }else{ out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: El mensaje no se ha desempaquetado correctamente.</font>"
	 * ); } }else{ out.
	 * println("<font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: Elemento no recibido ["
	 * +elem.value+"].</font>"); } } else if
	 * (request.getParameter("action").compareTo("Leer") == 0) { if(!firstRead){
	 * out.
	 * println("<font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: Elemento no leido [Buffer vacio].</font>"
	 * ); }else{ elem = new StringHolder(); //leer XML del servidor en forma de
	 * String if(bufferImpl.read(elem)){ out.
	 * println("<font face=\"Arial,Helvetica\" size=\"-1\">Leyendo primer elemento</font>"
	 * ); empaquetador = new CoderXML(nombre_archivo); //crear archivo XML leido a
	 * partir del String if(empaquetador.empaquetar(elem.value)){ desempaquetador =
	 * new DecoderXML(empaquetador.getRutaArchivoXML()); //desempaquetar datos del
	 * archivo XML Pair<Boolean, ArrayList<Mensaje>> nuevo = new Pair<Boolean,
	 * ArrayList<Mensaje>>(desempaquetador.desempaquetar()); if(nuevo.first()){ for
	 * (Mensaje mensaje : nuevo.second()) { out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"blue\" size=\"-1\">Mensaje recibido:</font>"
	 * ); out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"blue\" size=\"-1\">Fecha: "
	 * +mensaje.getDateOfNews()+"</font>"); out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"blue\" size=\"-1\">Descripción corta: "
	 * +mensaje.getShortDescription()+"</font>"); out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"blue\" size=\"-1\">Descripción larga: "
	 * +mensaje.getLongDescription()+"</font>"); out.println("<br>"); } }else{ out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: El mensaje no se ha desempaquetado correctamente.</font>"
	 * ); } }else{ out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: El mensaje no se ha desempaquetado correctamente.</font>"
	 * ); } }else{ out.
	 * println("<font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: Elemento no leido [Buffer vacio].</font>"
	 * ); } } } else if (request.getParameter("action").compareTo("Ver buffer") ==
	 * 0) { if(!firstRead){ out.
	 * println("<font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: Elementos no leidos [Buffer vacio].</font>"
	 * ); }else{ elem = new StringHolder(); //leer XML del servidor en forma de
	 * String (el XML contiene varios mensajes) if(bufferImpl.readAll(elem)){ out.
	 * println("<font face=\"Arial,Helvetica\" size=\"-1\">Leyendo elementos.</font>"
	 * ); empaquetador = new CoderXML(nombre_archivo); //crear archivo XML (con
	 * todos los mensajes) leido a partir del String
	 * if(empaquetador.empaquetarTodos(elem.value)){ desempaquetador = new
	 * DecoderXML(empaquetador.getRutaArchivoXML()); //desempaquetar datos (todos
	 * los mensajes) del archivo XML Pair<Boolean, ArrayList<Mensaje>> nuevo = new
	 * Pair<Boolean, ArrayList<Mensaje>>(desempaquetador.desempaquetar());
	 * if(nuevo.first()){ out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"green\" size=\"-1\">El buffer contiene "
	 * +nuevo.second().size()+" elementos.</font>"); for (Mensaje mensaje :
	 * nuevo.second()) { out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"blue\" size=\"-1\">Mensaje recibido:</font>"
	 * ); out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"blue\" size=\"-1\">Fecha: "
	 * +mensaje.getDateOfNews()+"</font>"); out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"blue\" size=\"-1\">Descripción corta: "
	 * +mensaje.getShortDescription()+"</font>"); out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"blue\" size=\"-1\">Descripción larga: "
	 * +mensaje.getLongDescription()+"</font>"); out.println("<br>"); } }else{ out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: Los mensajes no se han desempaquetado correctamente.</font>"
	 * ); } }else{ out.
	 * println("<br><font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: Los mensajes no se han desempaquetado correctamente.</font>"
	 * ); } }else{ out.
	 * println("<font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: Elementos no leidos [Buffer vacio].</font>"
	 * ); } } } else if (request.getParameter("action").compareTo("Longitud buffer")
	 * == 0) { String pBufferLength =
	 * request.getParameter("pBufferLength").toString();
	 * 
	 * if(pBufferLength.compareTo("") == 0 || pBufferLength.compareTo("0") == 0) {
	 * out.
	 * println("<font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: La longitud del buffer no puede ser vacía.</font>"
	 * ); } else if(!isNumeric(pBufferLength)) { out.
	 * println("<font face=\"Arial,Helvetica\" color=\"red\" size=\"-1\">[ERROR]: La longitud del buffer debe ser un numero entero.</font>"
	 * ); } else { bufferImpl.setBufferLength(pBufferLength);
	 * this.bufferImpl.getBufferLength(bufferLength); out.
	 * println("<font face=\"Arial,Helvetica\" color=\"green\" size=\"-1\">La longitud del buffer se actualizó correctamente: "
	 * + bufferLength.value + "</font>"); } } else if
	 * (request.getParameter("action").compareTo("Get longitud") == 0) {
	 * this.bufferImpl.getBufferLength(bufferLength); out.
	 * println("<font face=\"Arial,Helvetica\" color=\"green\" size=\"-1\">La longitud del buffer es: "
	 * + bufferLength.value + "</font>"); } else {
	 * out.println("<font face=\"Arial,Helvetica\" size=\"-1\">Accion '" +
	 * request.getParameter("action") + "' no reconocida.</font>"); }
	 * out.println("</td></tr></table>"); out.println("</body></html>"); }
	 */

	/********* metodos que se llaman desde un servio de angular) ******/

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// codigo de la accion Enviar
		PrintWriter out = response.getWriter();

		String dateOfNews = request.getParameter("fecha").toString();
		String shortDescription = request.getParameter("descripcion_corta").toString();
		String longDescription = request.getParameter("descripcion_larga").toString();

		if (dateOfNews.compareTo("") == 0) {
			out.println("La fecha no puede estar vacía");

		} else if (!dateOfNews.matches("(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}")) {
			out.println(" La fecha no tiene el formato válido dd/mm/aaaa");

		} else if (shortDescription.compareTo("") == 0 || shortDescription.replace("/\s+/g", "").length() < 5) {
			out.println(
					"La descripción corta de la noticia no puede estar vacía, ni puede tener menos de 5 caracteres");

		} else if (longDescription.compareTo("") == 0 || longDescription.replace("/\s+/g", "").length() < 20) {
			out.println(
					"La descripción larga de la noticia no puede estar vacía, ni puede tener menos de 20 caracteres.");

		} else if (shortDescription.replace("/\s+/g", "").length() > 30) {
			out.println("La descripción corta no puede tener más de 30 caracteres.");

		} else if (longDescription.replace("/\s+/g", "").length() > 500) {
			out.println("La descripción larga no puede tener más de 500 caracteres");

		} else {
			ArrayList<String> datesOfNews = new ArrayList<String>(); // lista de fechas
			ArrayList<String> shortDescriptions = new ArrayList<String>(); // lista de descripciones cortas
			ArrayList<String> longDescriptions = new ArrayList<String>(); // lista de descripciones largas
			datesOfNews.add(request.getParameter("fecha")); // para obtener valor de la fecha
			shortDescriptions.add(request.getParameter("descripcion_corta")); // para obtener valor de la descripcion
																				// corta
			longDescriptions.add(request.getParameter("descripcion_larga")); // para obtener valor de la descripcion
																				// larga

			/* proceso de empaquetar el mensaje a formato XML */
			empaquetador = new CoderXML(nombre_archivo);
			// crear archivo XML (empaquetar)
			if (empaquetador.empaquetar(datesOfNews, shortDescriptions, longDescriptions)) {
				System.out.println("El documento XML se ha creado correctamente");
				String xsd = System.getProperty("user.home") + File.separator + "productor_consumidor_files"
						+ File.separator + "documentoProductorConsumidor.xsd";
				Parser parser = new Parser(ruta_archivo);
				// validar sintacticamente el mensaje XML (antes de ser enviado)
				if (parser.validar()) {
					File xsd_file = new File(xsd);
					if (!xsd_file.exists()) {
					} else {
						validador = new Validador(empaquetador.getRutaArchivoXML(), xsd);
						// validar archivo XML semanticamente (antes de ser enviado)
						if (validador.validar()) {
							System.out.println("El documento XML ha sido validado semanticamente de forma correcta");
							String mensaje = new String(empaquetador.obtenerXMLComoString());

							if (bufferImpl.put(mensaje)) {
								System.out.println("Valor de firstRead en el enviar:" + firstRead);
								firstRead = true;
								 System.out.println("El documento XML ha sido almacenado correctamente en el buffer del servidor");
							} else {
								System.out.print("El documento XML no ha sido almacenado en el buffer del servidor [Pila llena].");
							}
						} else {
							System.out.println("El documento XML no es valido");
							System.out.println("El documento XML no ha sido almacenado en el buffer del servidor");
						}
					}
				} else {
					System.out.print(" Documento XML no valido sintacticamente");
				}
			} else {
				System.out.print(" No se ha podido almacenar el documento XML");
			}
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// codigo para la accion Leer
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		
		List<Noticia> noticias = new ArrayList<>();
		System.out.print("valor de firstRead:" + firstRead);
		if (!firstRead) {
			System.out.print("Elemento no leido [Buffer vacio].");
		} else {
			elem = new StringHolder();
			// leer XML del servidor en forma de String
			if (bufferImpl.readAll(elem)) {
				empaquetador = new CoderXML(nombre_archivo);
				// crear archivo XML leido a partir del String
				if (empaquetador.empaquetarTodos(elem.value)) {
					desempaquetador = new DecoderXML(empaquetador.getRutaArchivoXML());
					// desempaquetar datos del archivo XML
					Pair<Boolean, ArrayList<Noticia>> nuevo = new Pair<Boolean, ArrayList<Noticia>>(
							desempaquetador.desempaquetar());
					if (nuevo.first()) {
						for (Noticia noticia : nuevo.second()) {
							/*out.println("Mensaje recibido");
							out.println("Fecha: " + noticia.getDateOfNews() + "");
							out.println("Descripción corta: " + noticia.getShortDescription() + "");
							out.println("Descripción larga: " + noticia.getLongDescription() + "");
							out.println("");*/
							noticias.add(noticia);
						}
					} else {
						System.out.print("El mensaje no se ha desempaquetado correctamente");
					}
				} else {
					System.out.print("El mensaje no se ha desempaquetado correctamente");
				}
			} else {
				System.out.print("Elemento no leido [Buffer vacio].");
			}

		}
		
	   Gson gson = new Gson();
		String jsonData = gson.toJson(noticias);
		try {
            out.println(jsonData);
           // out.flush();   
            
        } finally {
            out.close();
        }
	}
}
