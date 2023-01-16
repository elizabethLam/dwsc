package ual.master.dswc.eliminarcomentario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ual.master.dswc.eliminarcomentario.model.Comentario;
import ual.master.dswc.eliminarcomentario.service.ComentarioService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ComentarioController {

	@Autowired
	private ComentarioService service;

	@GetMapping("/coment")
	public ResponseEntity<List<Comentario>> insertComentario(@RequestParam("autor") String nombreAutor) {
		List<Comentario> comentarios = service.findByNombreAutor(nombreAutor);
		return new ResponseEntity<List<Comentario>>(comentarios, HttpStatus.OK);
	}
	
	@DeleteMapping("/coment/{id}")
	public ResponseEntity<Void> eliminarComentario(@PathVariable Long id) {
		service.eliminarComentario(id);
		return ResponseEntity.noContent().build();	
	}
	

}
