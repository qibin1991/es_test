package com.es.neo4j.repository;


import com.es.neo4j.entity.FollowRelation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRelationRepository extends Neo4jRepository<FollowRelation,Long> {
}
