package ual.master.dwsc.insertapelicula.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("COMPROBAR-PELICULA")
public interface IPeliculaCliente {
	
	@GetMapping("/")
	public ResponseEntity<Boolean> validarPelicula(@RequestParam String nombre);

}
