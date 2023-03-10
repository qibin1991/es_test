package com.es.neo4j.repository;


import com.es.neo4j.entity.TopicRelation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRelationRepository extends Neo4jRepository<TopicRelation,Long> {
}
