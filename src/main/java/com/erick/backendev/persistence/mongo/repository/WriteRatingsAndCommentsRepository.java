package com.erick.backendev.persistence.mongo.repository;

import com.erick.backendev.persistence.mongo.document.WriteRatingsAndCommentsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WriteRatingsAndCommentsRepository extends MongoRepository<WriteRatingsAndCommentsDocument, String> {

    List<WriteRatingsAndCommentsDocument> findByShowId(Long showId);
}
