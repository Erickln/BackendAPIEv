package com.erick.backendev.persistence.mongo.repository;

import com.erick.backendev.persistence.mongo.document.WriteRatingsAndCommentsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WriteRatingsAndCommentsRepository extends MongoRepository<WriteRatingsAndCommentsDocument, String> {
}
