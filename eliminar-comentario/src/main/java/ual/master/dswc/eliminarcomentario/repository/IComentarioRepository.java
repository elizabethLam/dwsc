package ual.master.dswc.eliminarcomentario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ual.master.dswc.eliminarcomentario.model.Comentario;


public interface IComentarioRepository extends JpaRepository<Comentario, Long>{
	
	List<Comentario> findByNombreAutor(String nombreAutor);
	

}
