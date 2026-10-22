package com.erick.backendev.features.search.controller;

import com.erick.backendev.features.search.dto.ShowSearchResultResponseLITE;
import com.erick.backendev.features.search.service.ShowSearchService;

import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/shows")
public class ShowSearchController {

    private final ShowSearchService showSearchService;

    public ShowSearchController(ShowSearchService showSearchService) {
        this.showSearchService = showSearchService;
    }

    @GetMapping(value = "/search", params = "search_query")
    public List<ShowSearchResultResponseLITE> searchShowsByQuery(
            @RequestParam("search_query") @NotBlank String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be blank");
        }
        return showSearchService.searchShowsByQuery(searchQuery);
    }

    @GetMapping(value = "/{showId}")
    public Map<String, Object> getShowById(
            @PathVariable("showId") Long showId) {
        if (showId == null || showId <= 0) {
            throw new IllegalArgumentException("Invalid show ID: " + showId);
        }
        return showSearchService.getShowById(showId);
    }

    @PostMapping("/search")
    public ResponseEntity<HttpStatus> saveRatingAndComment(
            @RequestParam("show_id") Long showId,
            @RequestParam("comment") String comment,
            @RequestParam("rating") Integer rating) {

        if (showId == null || rating == null || rating < 1 || rating > 5) {
            return ResponseEntity.badRequest().build();
        }

        HttpStatus status = showSearchService.saveRatingAndComment(showId, comment, rating);
        return ResponseEntity.status(status).build();
    }

}
