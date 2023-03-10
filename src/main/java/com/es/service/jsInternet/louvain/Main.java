package com.es.service.jsInternet.louvain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    static void writeOutputCluster(String fileName, Louvain a) throws IOException {
        BufferedWriter bufferedWriter;
        bufferedWriter = new BufferedWriter(new FileWriter(fileName));
        for (int i = 0; i < a.global_n; i++) {
            bufferedWriter.write(Integer.toString(a.global_cluster[i]));
            bufferedWriter.write("\n");
        }
        bufferedWriter.close();
    }

    static void writeOutputCircle(String fileName, Louvain a) throws IOException {
        BufferedWriter bufferedWriter;
        bufferedWriter = new BufferedWriter(new FileWriter(fileName));
        ArrayList list[] = new ArrayList[a.global_n];
        for (int i = 0; i < a.global_n; i++) {
            list[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < a.global_n; i++) {
            list[a.global_cluster[i]].add(i);
        }
        for (int i = 0; i < a.global_n; i++) {
            if (list[i].size() == 0) {
                continue;
            }
            for (int j = 0; j < list[i].size(); j++) {
                bufferedWriter.write(list[i].get(j).toString() + " ");
            }
            bufferedWriter.write("\n");
        }
        bufferedWriter.close();
    }

    public static void main(String[] args) throws IOException {
        Louvain a = new Louvain();
        double beginTime = System.currentTimeMillis();
        a.init("/Users/lizheng/workspace/social_analysis/src/main/resources/facebook_combined.txt");
        a.louvain();
        a.print();
        double endTime = System.currentTimeMillis();
//        writeOutputCluster("/Users/lizheng/workspace/social_analysis/src/main/resources/cluster.txt", a);  //打印每个节点属于哪个簇
//        writeOutputCircle("/Users/lizheng/workspace/social_analysis/src/main/resources/circle.txt", a);   //打印每个簇有哪些节点
        System.out.format("Louvain算法计算时间: %f seconds%n", (endTime - beginTime) / 1000.0);
    }

}  