package ual.master.dswc.eliminarcomentario.exception;

import org.springframework.http.HttpStatus;

import org.springframework.web.server.ResponseStatusException;

public class CustomNotFoundException extends ResponseStatusException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CustomNotFoundException(HttpStatus code, String message) {
		super(code,message);
	}
	


}
