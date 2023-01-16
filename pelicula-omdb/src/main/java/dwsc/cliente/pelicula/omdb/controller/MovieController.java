package dwsc.cliente.pelicula.omdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dwsc.cliente.pelicula.omdb.service.IMovieService;


@RestController
public class MovieController {
	
	@Autowired
	private IMovieService service;
	
	
	@GetMapping("/")
	public ResponseEntity<Boolean> validarPelicula(@RequestParam String nombre) {
		
		return new ResponseEntity<Boolean>(service.validarPelicula(nombre), HttpStatus.OK);
				
	}


}
