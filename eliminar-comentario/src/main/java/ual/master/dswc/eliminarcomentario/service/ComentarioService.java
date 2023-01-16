package ual.master.dswc.eliminarcomentario.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ual.master.dswc.eliminarcomentario.exception.CustomNotFoundException;
import ual.master.dswc.eliminarcomentario.model.Comentario;



@Service
public class ComentarioService {
	
	@Autowired
	private ual.master.dswc.eliminarcomentario.repository.IComentarioRepository repository;
	
	
	public List<Comentario> findByNombreAutor(String nombreAutor) {
	    List<Comentario> comentarios= repository.findByNombreAutor(nombreAutor);
	    if(comentarios.isEmpty()) {
	    	throw new CustomNotFoundException(HttpStatus.NOT_FOUND,
					"No se ha encontrado ningún comentario que coincida con el nombre de autor especificado.");
	    }
	    return comentarios;    
	}
	
	public void eliminarComentario(Long id) {
		repository.deleteById(id);
	}

}
