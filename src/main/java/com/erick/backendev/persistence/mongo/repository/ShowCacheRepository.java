package com.erick.backendev.persistence.mongo.repository;

import com.erick.backendev.persistence.mongo.document.ShowCacheDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShowCacheRepository extends MongoRepository<ShowCacheDocument, Long> {
}
