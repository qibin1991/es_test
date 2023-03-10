package com.es.neo4j.repository;


import com.es.neo4j.entity.CommentRelation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRelationRepository extends Neo4jRepository<CommentRelation,Long> {
}
