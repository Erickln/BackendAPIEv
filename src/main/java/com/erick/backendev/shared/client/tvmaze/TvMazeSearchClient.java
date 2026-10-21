package com.erick.backendev.shared.client.tvmaze;

import com.erick.backendev.shared.client.tvmaze.TvMazeSearchClient.TvMazeChannel;
import com.erick.backendev.shared.client.tvmaze.TvMazeSearchClient.TvMazeClientException;
import com.erick.backendev.shared.client.tvmaze.TvMazeSearchClient.TvMazeSearchResult;
import com.erick.backendev.shared.client.tvmaze.TvMazeSearchClient.TvMazeShowDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;

@Component
public class TvMazeSearchClient {

	private final RestClient restClient;

    public TvMazeSearchClient(@Value("${tvmaze.base-url}") String baseUrl) {
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
            throw new TvMazeClientException("Could not retrieve search results from TVMaze by Query", ex);
        }
    }

    public TvMazeShowDTO searchShowsByID(String id) {
        try {
            TvMazeShowDTO response = restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/shows/{id}").build(id))
                    .retrieve()
                    .body(TvMazeShowDTO.class);
            return response;
        } catch (RestClientException ex) {
            throw new TvMazeClientException("Could not retrieve show details from TVMaze by ID", ex);
        }
    }


	public static class TvMazeClientException extends RuntimeException {
		public TvMazeClientException(String message, Throwable cause) {
			super(message, cause);
		}
	}

    /**
     * We only need the show object from the search result, so we can ignore other fields.
     */
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

    /**
     * We only need the name of the network or the name of the web channel.
     */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record TvMazeChannel(String name) {
	}
}
