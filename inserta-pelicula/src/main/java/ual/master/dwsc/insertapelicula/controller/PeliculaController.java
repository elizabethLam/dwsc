package ual.master.dwsc.insertapelicula.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ual.master.dwsc.insertapelicula.model.Pelicula;
import ual.master.dwsc.insertapelicula.service.IPeliculaService;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PeliculaController {
	
	@Autowired
	private IPeliculaService service;

	
	@PostMapping("/movie")
	public ResponseEntity<Pelicula> insertMovie(@RequestBody Pelicula pelicula) {	
		return new ResponseEntity<Pelicula>(service.insertar(pelicula), HttpStatus.CREATED);
	}
	
}
