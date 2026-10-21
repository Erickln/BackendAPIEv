package com.erick.backendev.features.search.controller;

import com.erick.backendev.features.search.dto.ShowSearchItemResponse;
import com.erick.backendev.features.search.service.ShowSearchService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/shows")
public class ShowSearchController {

	private final ShowSearchService showSearchService;

	public ShowSearchController(ShowSearchService showSearchService) {
		this.showSearchService = showSearchService;
	}

    @GetMapping(value = "/search", params = "search_query")
    public List<ShowSearchItemResponse> searchShowsByQuery( 
            @RequestParam("search_query") @NotBlank String searchQuery) {
        return showSearchService.searchByQuery(searchQuery);
    }
}
