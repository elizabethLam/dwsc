package ual.dwsc.buscapelicula.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ual.dwsc.buscapelicula.model.Pelicula;

@Repository
public interface IPeliculaRepository extends JpaRepository<Pelicula, Long>{
	
	List<Pelicula> findByNombreContaining(String nombre);

}
