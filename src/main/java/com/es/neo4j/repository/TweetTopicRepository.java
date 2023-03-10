package com.es.neo4j.repository;

import com.es.neo4j.entity.TweetTopicVo;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetTopicRepository  extends Neo4jRepository<TweetTopicVo,Long> {
}
