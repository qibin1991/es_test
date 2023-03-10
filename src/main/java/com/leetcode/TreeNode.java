package com.leetcode;

import com.leetcode.MyTree.Node;

/**
 * @ClassName TreeNode
 * @Description TODO
 * @Author QiBin
 * @Date 2021/11/1413:51
 * @Version 1.0
 **/
public class TreeNode {

    public static void main(String[] args) {

// TODO Auto-generated method stub

        BinaryTreeNode root = new BinaryTreeNode(10);

        BinaryTreeNode six = new BinaryTreeNode(6);

        BinaryTreeNode four = new BinaryTreeNode(4);

        BinaryTreeNode eight = new BinaryTreeNode(8);

        BinaryTreeNode fourteen = new BinaryTreeNode(14);

        BinaryTreeNode twelve = new BinaryTreeNode(12);

        BinaryTreeNode sixteen = new BinaryTreeNode(16);

        root.leftNode = six;

        root.rightNode = fourteen;

        six.leftNode = four;

        six.rightNode = eight;

        four.leftNode = null;

        four.rightNode = null;

        eight.leftNode = null;

        eight.rightNode = null;

        fourteen.leftNode = twelve;

        fourteen.rightNode = sixteen;

        twelve.leftNode = null;

        twelve.rightNode = null;

        sixteen.rightNode = null;

        sixteen.leftNode = null;

        BinaryTreeNode result = convert(root);

//                BinaryTreeNode result=baseconvert(root, null);

        while (result != null) {

            System.out.println(result.element);

            result = result.rightNode;

        }





    }



    //中序遍历
    public void inOrderRecur(Node root) {
        if (root == null) {
            return;
        }

//        inOrderRecur(root.leftChild+"");
//        System.out.print(root.value + " -> ");
//        inOrderRecur(root.right);
    }


    public static BinaryTreeNode convert(BinaryTreeNode root) {

        BinaryTreeNode lastNode = null;

        lastNode = baseconvert(root, lastNode);

        BinaryTreeNode headNode = lastNode;

        while (headNode.leftNode != null)

            headNode = headNode.leftNode;

        return headNode;

    }

    public static BinaryTreeNode baseconvert(BinaryTreeNode root, BinaryTreeNode lastNode) {

        if (root == null)

            return lastNode;

        BinaryTreeNode current = root;

        if (current.leftNode != null)

            lastNode = baseconvert(current.leftNode, lastNode);

        current.leftNode = lastNode;

        if (lastNode != null)

            lastNode.rightNode = current;

        lastNode = current;

        if (current.rightNode != null)

            lastNode = baseconvert(current.rightNode, lastNode);

        return lastNode;

    }




}
