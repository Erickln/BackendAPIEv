package com.erick.backendev.shared.client.tvmaze;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class TvMazeShowClient {

	private final RestClient restClient;

	public TvMazeShowClient(@Value("${tvmaze.base-url:http://api.tvmaze.com}") String baseUrl) {
		this.restClient = RestClient.builder()
				.baseUrl(baseUrl)
				.build();
	}

	public List<TvMazeSearchResult> searchShowsByQuery(String query) {
		try {
			TvMazeSearchResult[] response = restClient.get()
					.uri(uriBuilder -> uriBuilder.path("/search/shows").queryParam("q", query).build())
					.retrieve()
					.body(TvMazeSearchResult[].class);
			if (response == null) {
				return Collections.emptyList();
			}
			return List.of(response);
		} catch (RestClientException ex) {
			throw new TvMazeClientException("Could not retrieve search results from TVMaze by query", ex);
		}
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

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record TvMazeSearchResult(TvMazeShowDTO show) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record TvMazeShowDTO(
			Long id,
			String name,
			TvMazeChannel network,
			TvMazeChannel webChannel,
			String summary,
			List<String> genres
	) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record TvMazeChannel(String name) {
	}
}
