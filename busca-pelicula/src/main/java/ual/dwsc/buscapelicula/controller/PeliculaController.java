package ual.dwsc.buscapelicula.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ual.dwsc.buscapelicula.model.Pelicula;

import ual.dwsc.buscapelicula.repository.IPeliculaRepository;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PeliculaController {
	
	@Autowired
	private IPeliculaRepository repository;
	
	@GetMapping("/movie")
    public List<Pelicula> findByName(@RequestParam("name") String name){
        return repository.findByNombreContaining(name);
    }

}
