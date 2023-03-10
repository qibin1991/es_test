package com.es.service.jsInternet.pagerank;

public class PageRankNode implements Comparable<PageRankNode> {

    private String identifier = null;
    private double rank = 0;

    public PageRankNode(String identifier, double rank) {
        this.identifier = identifier;
        this.rank = rank;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    @Override
    public int compareTo(PageRankNode other) {
        return rank > other.getRank()
                ? -1 : rank < other.getRank()
                ? 1 : 0;
    }
}
