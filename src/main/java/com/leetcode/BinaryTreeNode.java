package com.leetcode;

/**
 * @ClassName BinaryTreeNode
 * @Description TODO
 * @Author QiBin
 * @Date 2021/11/1413:55
 * @Version 1.0
 **/
public class BinaryTreeNode {
    protected int element;

    protected BinaryTreeNode leftNode;

    protected BinaryTreeNode rightNode;

    public BinaryTreeNode(int obj) {
        this.element = obj;

        this.leftNode = null;

        this.rightNode = null;

    }

    ///合并构建声明

    public BinaryTreeNode(int obj, BinaryTreeNode leftNode,

                          BinaryTreeNode rightNode) {
        element = obj;

        if (leftNode == null)

            this.leftNode = null;

        else

            this.leftNode = leftNode.getRootNode();

        if (rightNode == null)

            this.rightNode = null;

        else

            this.rightNode = rightNode.getRootNode();

    }

 
    private BinaryTreeNode getRootNode() {
        return new BinaryTreeNode(1);
    }

    public int numChildren() {
        int children = 0;

        if (leftNode != null)

            children = 1 + leftNode.numChildren();

        if (rightNode != null)

            children = children + 1 + rightNode.numChildren();

        return children;

    }

    public int getElement() {
        return element;

    }

    public BinaryTreeNode getrightNode() {
        return rightNode;

    }

    public void setrightNode(BinaryTreeNode node) {
        rightNode = node;

    }

    public BinaryTreeNode getleftNode() {
        return leftNode;

    }

    public void setleftNode(BinaryTreeNode node) {
        leftNode = node;

    }

    public boolean judge(){
        if(rightNode == null && leftNode == null)

            return true;

        else

            return false;

    }


}