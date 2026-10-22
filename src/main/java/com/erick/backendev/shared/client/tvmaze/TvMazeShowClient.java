package com.erick.backendev.shared.client.tvmaze;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Component
public class TvMazeShowClient {

	private final RestClient restClient;

	public TvMazeShowClient(@Value("${tvmaze.base-url:http://api.tvmaze.com}") String baseUrl) {
		this.restClient = RestClient.builder()
				.baseUrl(baseUrl)
				.build();
	}

	public Map<String, Object> getShow(Long showId) {
		try {
			Map<String, Object> response = restClient.get()
					.uri("/shows/{showId}", showId)
					.retrieve()
					.body(new ParameterizedTypeReference<>() {
					});
			return response == null ? Map.of() : response;
		} catch (RestClientException ex) {
			throw new TvMazeClientException("Could not retrieve show details from TVMaze", ex);
		}
	}

	public static class TvMazeClientException extends RuntimeException {
		public TvMazeClientException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
