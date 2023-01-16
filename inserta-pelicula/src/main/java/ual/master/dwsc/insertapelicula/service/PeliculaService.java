package ual.master.dwsc.insertapelicula.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ual.master.dwsc.insertapelicula.client.IPeliculaCliente;
import ual.master.dwsc.insertapelicula.exception.CustomNotFoundException;
import ual.master.dwsc.insertapelicula.model.Pelicula;
import ual.master.dwsc.insertapelicula.repository.IPeliculaRepository;

@Service
public class PeliculaService implements IPeliculaService{
	
	@Autowired
	private IPeliculaCliente peliculaCliente;
	
	@Autowired
	private IPeliculaRepository repository;

	@Override
	public Pelicula insertar(Pelicula pelicula) {
		Pelicula peliculaInsertada = new Pelicula();
		try {
			ResponseEntity<Boolean> response = peliculaCliente.validarPelicula(pelicula.getNombre());
			
			if (!response.getBody()) {
				throw new CustomNotFoundException(HttpStatus.NOT_FOUND,
						"No se ha encontrado la pelicula que desea insertar.");
			} 
			peliculaInsertada = repository.save(pelicula);
			
		} catch (feign.FeignException.ServiceUnavailable e) {
			throw new CustomNotFoundException(HttpStatus.NOT_FOUND,
					"No se ha encontrado el microservicio Comprobar-pelicula.");

		}
		return peliculaInsertada;
	}

}
