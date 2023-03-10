package com.leetcode.MyTree;

import lombok.Data;

/**
 * @ClassName Node
 * @Description TODO
 * @Author QiBin
 * @Date 2021/11/1414:13
 * @Version 1.0
 **/


@Data
public class Node {

    Object value;
    Node leftChild;
    Node rightChild;

    public Node(Object value){
        super();
        this.value=value;
    }

    public Node(Object value, Node leftChild, Node rightChild) {
        this.value = value;
        this.leftChild = leftChild;
        this.rightChild = rightChild;

    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                ", leftChild=" + leftChild +
                ", rightChild=" + rightChild +
                '}';
    }
}

