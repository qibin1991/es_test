package com.es.neo4j.repository;


import com.es.neo4j.entity.DiscussRelation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussRelationRepository extends Neo4jRepository<DiscussRelation,Long> {
}
