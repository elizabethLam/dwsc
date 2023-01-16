package ual.master.dwsc.comentario.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ual.master.dwsc.comentario.model.Comentario;
import ual.master.dwsc.comentario.model.Pelicula;
import ual.master.dwsc.comentario.repository.IComentarioRepository;

@Service
public class ComentarioService {
	
	@Autowired
	private IComentarioRepository repository;
	
	
	public void adicionarComentario(Comentario comentario,Long idPelicula) {
		Pelicula pelicula = new Pelicula();
		pelicula.setId(idPelicula);
		comentario.setPelicula(pelicula);
		comentario.setFecha(new Date());
		repository.save(comentario);	
	}

}
