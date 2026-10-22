package com.erick.backendev.persistence.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document(collection = "ratings_and_comments")
public class WriteRatingsAndCommentsDocument {

    @Id
    private String id;

    private Long showId;
    private Integer rating;
    private String comment;

    public WriteRatingsAndCommentsDocument() {
    }

    public WriteRatingsAndCommentsDocument(String id, Long showId, Integer rating, String comment) {
        this.id = id;
        this.showId = showId;
        this.rating = rating;
        this.comment = comment;
    }

    public WriteRatingsAndCommentsDocument(Long showId, String comment, Integer rating) {
        this(null, showId, rating, comment);
    }
}