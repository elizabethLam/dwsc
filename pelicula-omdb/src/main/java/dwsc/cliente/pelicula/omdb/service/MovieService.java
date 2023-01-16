package dwsc.cliente.pelicula.omdb.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dwsc.cliente.pelicula.omdb.util.CustomResponse;

@Service
public class MovieService implements IMovieService {
	
	@Autowired
	RestTemplate restTemplate;
	
	public Boolean validarPelicula(String nombre) {
		Boolean apiResponse = false;
		try {
			ResponseEntity<String> result = restTemplate
					.getForEntity("http://www.omdbapi.com/?t=" + nombre + "&apiKey=bf1d31e7", String.class);

			JSONObject data = new JSONObject(result.getBody());
			apiResponse = (Boolean)data.getBoolean("Response");
			

		} catch (JSONException e) {
			e.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return apiResponse;
	}

}
