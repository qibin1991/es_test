package com.es.neo4j.repository;


import com.es.neo4j.entity.TweetFollowVo;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetFollowRepository  extends Neo4jRepository<TweetFollowVo,Long> {
}
