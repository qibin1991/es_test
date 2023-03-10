package com.es.neo4j.repository;


import com.es.neo4j.entity.ForwardRelation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForwardRelationRepository  extends Neo4jRepository<ForwardRelation,Long> {
}
