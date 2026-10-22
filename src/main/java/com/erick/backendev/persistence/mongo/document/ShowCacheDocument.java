package com.erick.backendev.persistence.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter  
@Document(collection = "shows")
public class ShowCacheDocument {

	@Id
	private Long id;

	@Field("payload")
	private Map<String, Object> payload;

	public ShowCacheDocument() {
	}

	public ShowCacheDocument(Long id, Map<String, Object> payload) {
		this.id = id;
		this.payload = payload;
	}
}
