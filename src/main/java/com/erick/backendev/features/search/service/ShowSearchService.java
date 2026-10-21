package com.erick.backendev.features.search.service;

import com.erick.backendev.features.search.dto.ShowSearchItemResponse;
import com.erick.backendev.shared.client.tvmaze.TvMazeSearchClient;
import com.erick.backendev.shared.client.tvmaze.TvMazeSearchClient.TvMazeSearchResult;
import com.erick.backendev.shared.client.tvmaze.TvMazeSearchClient.TvMazeShowDTO;

import jakarta.validation.constraints.NotBlank;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ShowSearchService {

	private final TvMazeSearchClient tvMazeSearchClient;

	public ShowSearchService(TvMazeSearchClient tvMazeSearchClient) {
		this.tvMazeSearchClient = tvMazeSearchClient;
	}

    public List<ShowSearchItemResponse> searchByQuery(String searchQuery) {
        return mapToResponseList(tvMazeSearchClient.searchShowsByQuery(searchQuery));
    }

    public TvMazeShowDTO searchByID(String id) {
        TvMazeShowDTO clientResult = tvMazeSearchClient.searchShowsByID(id);
        if (clientResult == null) {
            throw new IllegalArgumentException("No show found with ID: " + id);
        }
        return clientResult;
    }

    private List<ShowSearchItemResponse> mapToResponseList(List<TvMazeSearchResult> searchResults) {
        return searchResults.stream()
                .map(TvMazeSearchResult::show)
                .filter(Objects::nonNull)
                .map(show -> new ShowSearchItemResponse(
                        show.id(),
                        show.name(),
                        resolveChannel(show.network(), show.webChannel(), show.id()),
                        show.summary(),
                        show.genres()
                ))
                .toList();
    }
    
	private String resolveChannel(TvMazeSearchClient.TvMazeChannel network,
							  TvMazeSearchClient.TvMazeChannel webChannel,
                              Long id) {
		if (network != null && network.name() != null && !network.name().isBlank()) {
			return network.name();
		}
		if (webChannel != null && webChannel.name() != null && !webChannel.name().isBlank()) {
			return webChannel.name();
		}
        System.out.println("No valid channel name found for show ID: " + id);
		return null;
	}
}
