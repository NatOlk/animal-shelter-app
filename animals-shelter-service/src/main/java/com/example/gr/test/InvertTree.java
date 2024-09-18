package com.example.gr.test;

public class InvertTree {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public static TreeNode invertTree(TreeNode root) {
        if (root == null) return null;

        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);

        root.left = right;
        root.right = left;

        return root;
    }


    public static String printTree(TreeNode root) {
        if (root == null) return "";

        StringBuilder sb = new StringBuilder();
        sb.append(root.val).append(" ");
        sb.append(printTree(root.left));
        sb.append(printTree(root.right));
        return sb.toString();
    }

    public static void main(String[] args) {

        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(1);
        root.right = new TreeNode(2);
        root.right.left = new TreeNode(3);
        root.right.right = new TreeNode(1);
        System.out.println(printTree(root));
        System.out.println(printTree(invertTree(root)));
    }
}
