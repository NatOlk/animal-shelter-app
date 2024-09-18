package com.example.gr.test;

import java.util.ArrayList;
import java.util.List;

public class UnivalTree {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public static boolean isUnivalTree(TreeNode root) {
        if (root == null) {
            return true;
        }

        return isUnivalTree(root, root.val);
    }

    private static boolean isUnivalTree(TreeNode node, int val) {
        if (node == null) {
            return true;
        }

        if (node.val != val) {
            return false;
        }

        return isUnivalTree(node.left, val) && isUnivalTree(node.right, val);
    }

    public static void main(String[] args) {

     /*   TreeNode root = new TreeNode(1);
        root.left = new TreeNode(1);
        root.right = new TreeNode(1);
        root.right.left = new TreeNode(3);
        root.right.right = new TreeNode(1);
        System.out.println(isUnivalTree(root));*/
        List<String> arl = new ArrayList<>();
        arl.add(0, "5Element");

        arl.add(0, "Nel");
        System.out.println("Ar " + arl);
     //   System.out.println("GK " + arl.getFirst());
    }
}
