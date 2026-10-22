package com.erick.backendev.features.search.dto;

import java.util.List;

public record ShowSearchResultResponseLITE(
		Long id,
		String name,
		String channel,
		String summary,
		List<String> genres
) {
}
