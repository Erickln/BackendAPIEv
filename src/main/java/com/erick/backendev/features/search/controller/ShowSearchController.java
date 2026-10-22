package com.erick.backendev.features.search.controller;

import com.erick.backendev.features.search.dto.ShowSearchResultResponseLITE;
import com.erick.backendev.features.search.service.ShowSearchService;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        return showSearchService.searchShowsByQuery(searchQuery);
    }

    @GetMapping(value = "/{showId}")
    public Map<String, Object> getShowById(
            @PathVariable("showId") Long showId) {
        return showSearchService.getShowById(showId);
    }
}
