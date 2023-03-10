package com.es.neo4j.repository;


import com.es.neo4j.entity.TweetUserVo;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetUserRepository extends Neo4jRepository<TweetUserVo, Long> {

}
