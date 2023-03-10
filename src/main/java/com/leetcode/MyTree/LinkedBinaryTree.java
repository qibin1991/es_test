package com.leetcode.MyTree;

/**
 * @ClassName LinkedBinaryTree
 * @Description TODO
 * @Author QiBin
 * @Date 2021/11/1414:13
 * @Version 1.0
 **/


import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class LinkedBinaryTree implements BinaryTree {
    public Node root;
    private int size;

    public LinkedBinaryTree(Node root) {
        this.root = root;
    }

    @Override
    public Boolean isEmpty() {
        return root == null;
    }

    @Override
    public int size() {
        System.out.println("二叉树的节点个数：");
        return this.size(root);
    }
    public int size(Node root){
        if(root!=null){
            int nl=this.size(root.leftChild);


            int nr=this.size(root.rightChild);


            return nl+nr+1;

        }else{
            return 0;

        }

    }

    @Override
    public int getHeight() {
        System.out.println("二叉树的高度：");
        return this.getHeight(root);

    }
    public int getHeight(Node root){
        if(root!=null){
            //获取左子树的高度
            int nl=this.getHeight(root.leftChild);

            //获取右子树的高度
            int nr=this.getHeight(root.rightChild);

            //返回左子树，右子树的高度加1
            return nl>nr?nl+1:nr+1;

        }else{

            return 0;
        }



    }



    public void preOrderTraverse(){
        System.out.println("先序遍历");
        this.preOrderTraverse(this.root);
        System.out.println();

    }
    @Override
    public void preOrderTraverse(Node root) {
        /*
         * 1.输出根节点的值
         * 2.对左子树进行先序遍历
         * 3.对右子树进行先序遍历
         * */
        if(root!=null) {
            System.out.print(root.value+"  ");


            this.preOrderTraverse(root.leftChild);

            this.preOrderTraverse(root.rightChild);

        }
    }

    @Override
    public void inOrderTraverse() {
        System.out.println("中序遍历");
        this.inOrderTraverse(this.root);
        System.out.println();

    }


    public void inOrderTraverse(Node root) {
        if(root!=null){
            this.inOrderTraverse(root.leftChild);
            System.out.print(root.value+"   ");
            this.inOrderTraverse(root.rightChild);

        }

    }

    @Override
    public void postOrderTraverse() {
        System.out.println("后序遍历");
        this.postOrderTraverse(this.root);
        System.out.println();

    }

    @Override
    public void postOrderTraverse(Node root) {
        if(root!=null){
            this.postOrderTraverse(root.leftChild);
            this.postOrderTraverse(root.rightChild);
            System.out.print(root.value+"   ");
        }

    }

    public Node findKey(int value){

        return this.findKey(value,root);
    }

    @Override
    public void levelOrderByStack() {
        System.out.println("按照层次遍历二叉树");
        if(root == null){
            return;
        }
        Queue<Node> queue=new LinkedList<Node>();//队列
        queue.add(root);
        while(queue.size()!=0){
            int len=queue.size();
            for(int i=0;i<len;i++){
                Node temp=queue.poll();
                System.out.print(temp.value+"  ");
                if(temp.leftChild!=null){
                    queue.add(temp.leftChild);

                }
                if(temp.rightChild!=null){
                    queue.add(temp.rightChild);
                }
            }

        }

        System.out.println();
    }

    public Node findKey(Object value, Node root){
        //结点为空 可能是整个树的根节点 也可能是递归调用中叶子结点的左孩子和有孩子
        if(root ==null){
            return null;
        }else if(root!=null&&root.value==value){//找到了 结束条件1
            return root;
        }else {
            //递归调用
            Node node1=this.findKey(value,root.leftChild);
            Node node2=this.findKey(value,root.rightChild);
            if(node1!=null&& node1.value==value ){

                return node1;
            }else if(node2!=null&& node2.value==value) {

                return node2;
            }  else {
                return null;
            }
        }
    }

    //栈 后进的先出
    public void inOrderByStack(){
        System.out.println("中序非递归遍历");
        Deque<Node> stack=new LinkedList<Node>();//栈
        Node current=root;
        while(current!=null||!stack.isEmpty()){
            while(current!=null){
                stack.push(current);
                current=current.leftChild;
            }
            if(!stack.isEmpty()){
                current=stack.pop();
                System.out.print(current.value+"  ");
                current=current.rightChild;
            }
        }
        System.out.println();
    }


}

