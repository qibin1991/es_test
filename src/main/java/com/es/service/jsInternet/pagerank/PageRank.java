package com.es.service.jsInternet.pagerank;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>【描述】：pagerank算法</p>
 * <p>【方法】：</p>
 * <p>【参数】: </p>
 * <p>【作者】: lizheng</p>
 * <p>【日期】: 2019-08-05</p>
 **/
@Component
public class PageRank {
    private Map<String, ArrayList<MapEntry>> map = null;
    private List<PageRankNode> rankedList = null;

    public PageRank() {
        map = new HashMap<String, ArrayList<MapEntry>>();
    }

    /**
     * <p>【描述】：利用散列图构造图的链表表示</p>
     * <p>【方法】：initializeMap</p>
     * <p>【参数】: [nodeMaps]</p>
     * <p>【作者】: lizheng</p>
     * <p>【日期】: 2019-08-05</p>
     **/
    public void initializeMap(Map<String, Integer> nodeMaps) {

        Set<String> keys = nodeMaps.keySet();
        for (String key : keys) {
            String[] keyArray = key.split(",");
            if (keyArray.length != 2) {
                continue;
            }
            String source = keyArray[0];
            String target = keyArray[1];
            Integer number = nodeMaps.get(key);
//            this.addEntry(source, target, number);
//            this.addEntry(target, source, number);
            this.addEntry(source, target, 1);
            this.addEntry(target, source, 1);
        }
    }


    /**
     * <p>【描述】：无向图，添加项 (node1, <node2, weight>), (node2, <node1, weight>)</p>
     * <p>【方法】：addEntry</p>
     * <p>【参数】: [node1, node2, edgeWeight]</p>
     * <p>【作者】: lizheng</p>
     * <p>【日期】: 2019-08-05</p>
     **/
    public void addEntry(String node1, String node2, double edgeWeight) {
        MapEntry mapEntry = new MapEntry(node2, edgeWeight);
        if (this.map.containsKey(node1)) {
            if (!map.get(node1).contains(mapEntry)) {
                this.map.get(node1).add(mapEntry);
            }
        } else {
            ArrayList<MapEntry> list = new ArrayList<MapEntry>();
            list.add(mapEntry);
            this.map.put(node1, list);
        }
    }

    /**
     * <p>【描述】：排序</p>
     * <p>【方法】：rank</p>
     * <p>【参数】: [iterations, dampingFactor]</p>
     * <p>【作者】: lizheng</p>
     * <p>【日期】: 2019-08-05</p>
     **/
    public LinkedHashMap<String, Double> rank(int iterations, double dampingFactor) {
        HashMap<String, Double> lastRanking = new HashMap<String, Double>();
        HashMap<String, Double> nextRanking = new HashMap<String, Double>();
        Double startRank = 1.0 / map.size();
        for (String key : map.keySet()) {
            lastRanking.put(key, startRank);
        }
        double dampingFactorComplement = 1.0 - dampingFactor;
        for (int times = 0; times < iterations; times++) {
            for (String key : map.keySet()) {
                double totalWeight = 0;
                for (MapEntry entry : map.get(key)) {
                    totalWeight += (entry.getWeight() * lastRanking.get(entry.getIdentifier()) / this.map.get(entry.getIdentifier()).size());
                }

                Double nextRank = dampingFactorComplement
                        + (dampingFactor * totalWeight);

                nextRanking.put(key, nextRank);
            }
            lastRanking = nextRanking;
        }

        System.out.println(iterations + " times iteration finished...");

        rankedList = PageRankVector(lastRanking);


        List<Double> numList = new ArrayList<Double>();
        // 最终结果
        LinkedHashMap<String, Double> result = new LinkedHashMap<String, Double>();
        // 归一化处理
        for (PageRankNode node : rankedList) {
            System.out.println(node.getIdentifier() + "\t" + node.getRank() * 20);
            result.put(node.getIdentifier(), node.getRank() * 20);
//            numList.add(node.getRank());
        }

//        Double maxNum = Collections.max(numList);
//        Double minNum = Collections.min(numList);
//
//        for (PageRankNode node : rankedList) {
//            System.out.println(node.getIdentifier() + "\t" + ((node.getRank()-minNum)/(maxNum-minNum)));
//            result.put(node.getIdentifier(), ((node.getRank()-minNum)/(maxNum-minNum)));
//        }

        return result;
    }

    /**
     * <p>【描述】：使用集合的排序方法获取排名结果</p>
     * <p>【方法】：</p>
     * <p>【参数】: </p>
     * <p>【作者】: lizheng</p>
     * <p>【日期】: 2019-08-05</p>
     **/
    public List<PageRankNode> PageRankVector(final HashMap<String, Double> LastRanking) {
        List<PageRankNode> nodeList = new LinkedList<PageRankNode>();
        for (String identifier : LastRanking.keySet()) {
            PageRankNode node = new PageRankNode(identifier, LastRanking.get(identifier));
            nodeList.add(node);
        }
        Collections.sort(nodeList);
        return nodeList;
    }

    /**
     * <p>【描述】：运行PageRank算法</p>
     * <p>【方法】：pageRankRun</p>
     * <p>【参数】: [nodeMaps]</p>
     * <p>【作者】: lizheng</p>
     * <p>【日期】: 2019-08-06</p>
     **/
    public LinkedHashMap<String, Double> pageRankRun(Map<String, Integer> nodeMaps) {
        int iterations = 10;
        double DumpingFactor = 0.85;
        initializeMap(nodeMaps);
        return rank(iterations, DumpingFactor);
    }

}
