package ual.master.dwsc.insertapelicula.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ual.master.dwsc.insertapelicula.model.Pelicula;


public interface IPeliculaRepository extends JpaRepository<Pelicula,Long>{

}
