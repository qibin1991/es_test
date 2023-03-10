package com.es.neo4j.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.es.neo4j.entity.ForwardRelation;


public interface AnalysisSerive {

    void addForwardEdge(ForwardRelation forwardRelation) throws JsonProcessingException;
}
