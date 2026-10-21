package com.erick.backendev.features.search.dto;

import java.util.List;

public record ShowSearchItemResponse(
		Long id,
		String name,
		String channel,
		String summary,
		List<String> genres
) {
}
