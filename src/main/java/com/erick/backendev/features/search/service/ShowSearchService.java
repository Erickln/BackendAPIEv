package com.erick.backendev.features.search.service;

import com.erick.backendev.features.search.dto.ShowSearchResultResponseLITE;
import com.erick.backendev.persistence.mongo.document.ShowCacheDocument;
import com.erick.backendev.persistence.mongo.repository.ShowCacheRepository;
import com.erick.backendev.shared.client.tvmaze.TvMazeSearchClient;
import com.erick.backendev.shared.client.tvmaze.TvMazeSearchClient.TvMazeSearchResult;
import com.erick.backendev.shared.client.tvmaze.TvMazeShowClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class ShowSearchService {

	private final TvMazeSearchClient tvMazeSearchClient;
	private final ShowCacheRepository showCacheRepository;
	private final TvMazeShowClient tvMazeShowClient;

	public ShowSearchService(TvMazeSearchClient tvMazeSearchClient,
								ShowCacheRepository showCacheRepository,
								TvMazeShowClient tvMazeShowClient) {
		this.tvMazeSearchClient = tvMazeSearchClient;
		this.showCacheRepository = showCacheRepository;
		this.tvMazeShowClient = tvMazeShowClient;
	}

    public List<ShowSearchResultResponseLITE> searchShowsByQuery(String searchQuery) {
        return mapToResponseList(tvMazeSearchClient.searchShowsByQuery(searchQuery));
    }

    public Map<String, Object> getShowById(Long showId) {
        Optional<ShowCacheDocument> cached = showCacheRepository.findById(showId);
        if (cached.isPresent()) {
            System.out.println("Returning cached show for ID: " + showId);
            return cached.get().getPayload();
        }

        Map<String, Object> fetchedShow = tvMazeShowClient.getShow(showId);
        showCacheRepository.save(new ShowCacheDocument(showId, fetchedShow));
        return fetchedShow;
    }

    private List<ShowSearchResultResponseLITE> mapToResponseList(List<TvMazeSearchResult> searchResults) {
        return searchResults.stream()
                .map(TvMazeSearchResult::show)
                .filter(Objects::nonNull)
                .map(show -> new ShowSearchResultResponseLITE(
                        show.id(),
                        show.name(),
                        resolveChannel(show.network(), show.webChannel(), show.id()),
                        show.summary(),
                        show.genres()
                ))
                .toList();

    }
    
    /**
     * Only returns the channel name if it's not null or blank. If both network and webChannel are null or blank, it returns null.
     */
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
