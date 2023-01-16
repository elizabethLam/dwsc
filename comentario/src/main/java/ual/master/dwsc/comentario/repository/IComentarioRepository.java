package ual.master.dwsc.comentario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ual.master.dwsc.comentario.model.Comentario;

public interface IComentarioRepository extends JpaRepository<Comentario, Long>{
	

}
