package com.es.neo4j.repository;


import com.es.neo4j.entity.MentionRelation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentionRelationRepository extends Neo4jRepository<MentionRelation,Long> {
}
