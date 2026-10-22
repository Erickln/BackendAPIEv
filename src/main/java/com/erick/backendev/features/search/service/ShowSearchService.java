package com.erick.backendev.features.search.service;

import com.erick.backendev.features.search.dto.ShowCommentResponse;
import com.erick.backendev.features.search.dto.ShowSearchResultResponseLITE;
import com.erick.backendev.persistence.mongo.document.ShowCacheDocument;
import com.erick.backendev.persistence.mongo.document.WriteRatingsAndCommentsDocument;
import com.erick.backendev.persistence.mongo.repository.ShowCacheRepository;
import com.erick.backendev.persistence.mongo.repository.WriteRatingsAndCommentsRepository;
import com.erick.backendev.shared.client.tvmaze.TvMazeShowClient;
import com.erick.backendev.shared.client.tvmaze.TvMazeShowClient.TvMazeSearchResult;
import com.mongodb.DuplicateKeyException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class ShowSearchService {

	private final TvMazeShowClient tvMazeShowClient;
    
	private final ShowCacheRepository showCacheRepository;
    private final WriteRatingsAndCommentsRepository writeRatingsAndCommentsRepository;

	public ShowSearchService(TvMazeShowClient tvMazeShowClient,
								ShowCacheRepository showCacheRepository,
                                WriteRatingsAndCommentsRepository writeRatingsAndCommentsRepository) {
		this.tvMazeShowClient = tvMazeShowClient;
		this.showCacheRepository = showCacheRepository;
        this.writeRatingsAndCommentsRepository = writeRatingsAndCommentsRepository;
	}

    public List<ShowSearchResultResponseLITE> searchShowsByQuery(String searchQuery) {
        return mapToResponseList(tvMazeShowClient.searchShowsByQuery(searchQuery));
    }

    public Map<String, Object> getShowById(Long showId) {
        String cacheKey = String.valueOf(showId);
        Optional<ShowCacheDocument> cached = showCacheRepository.findById(cacheKey);
        if (cached.isPresent()) {
            System.out.println("Returning cached show for ID: " + showId);
            return attachComments(new HashMap<>(cached.get().getPayload()), showId);
        }

        Map<String, Object> fetchedShow = tvMazeShowClient.getShow(showId);
        showCacheRepository.save(new ShowCacheDocument(cacheKey, fetchedShow));
        return attachComments(new HashMap<>(fetchedShow), showId);
    }

    public HttpStatus saveRatingAndComment(Long showId, String comment, Integer rating) {
    try {
        writeRatingsAndCommentsRepository.save(
                new WriteRatingsAndCommentsDocument(showId, comment, rating));
        return HttpStatus.CREATED;
    } catch (DuplicateKeyException e) {
        return HttpStatus.CONFLICT;
    } catch (DataAccessException e) {
        System.err.println("Database access error while saving rating and comment: " + e.getMessage());
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}

    private List<ShowSearchResultResponseLITE> mapToResponseList(List<TvMazeSearchResult> searchResults) {
        if (searchResults == null || searchResults.isEmpty()) {
            return List.of();
        }
        return searchResults.stream()
                .map(TvMazeSearchResult::show)
                .filter(Objects::nonNull)
                .map(show -> new ShowSearchResultResponseLITE(
                        show.id(),
                        show.name(),
                        resolveChannel(show.network(), show.webChannel(), show.id()),
                        show.summary(),
                        show.genres(),
                        loadCommentsForShow(show.id())
                ))
                .toList();

    }

    private List<ShowCommentResponse> loadCommentsForShow(Long showId) {
        return writeRatingsAndCommentsRepository.findByShowId(showId).stream()
                .map(document -> new ShowCommentResponse(document.getComment(), document.getRating()))
                .toList();
    }

    private Map<String, Object> attachComments(Map<String, Object> show, Long showId) {
        List<Map<String, Object>> comments = writeRatingsAndCommentsRepository.findByShowId(showId).stream()
                .map(document -> {
                    Map<String, Object> comment = new HashMap<>();
                    comment.put("comment", document.getComment());
                    comment.put("rating", document.getRating());
                    return comment;
                })
                .toList();
        show.put("comments", comments);
        return show;
    }
    
    /**
     * Only returns the channel name if it's not null or blank. If both network and webChannel are null or blank, it returns null.
     */
	private String resolveChannel(TvMazeShowClient.TvMazeChannel network,
								  TvMazeShowClient.TvMazeChannel webChannel,
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
