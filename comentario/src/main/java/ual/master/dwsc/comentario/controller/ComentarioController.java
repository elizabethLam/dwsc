package ual.master.dwsc.comentario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ual.master.dwsc.comentario.model.Comentario;
import ual.master.dwsc.comentario.service.ComentarioService;

@RestController
@CrossOrigin(origins = {"http://localhost:3333", "http://localhost:4200"})
public class ComentarioController {

	@Autowired
	private ComentarioService service;

	@PostMapping("/coment/{id-pelicula}")
	public ResponseEntity insertComentario(@PathVariable("id-pelicula") Long id,@RequestBody Comentario comentario) {
		service.adicionarComentario(comentario, id);
		return ResponseEntity.status(HttpStatus.CREATED).build();

	}

}
