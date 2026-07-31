package com.wandersync.backend.repository;

import com.wandersync.backend.model.TravelKnowledgeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelKnowledgeDocumentRepository extends MongoRepository<TravelKnowledgeDocument, String> {
}
