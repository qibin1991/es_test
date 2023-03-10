package com.es.neo4j.repository;


import com.es.neo4j.entity.TweetsVo;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetsRepository  extends Neo4jRepository<TweetsVo,Long> {
}
