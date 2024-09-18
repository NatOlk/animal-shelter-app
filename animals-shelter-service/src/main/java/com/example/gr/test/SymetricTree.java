package com.example.gr.test;

public class SymetricTree {
    public static class TreeNode {
        int val;
        int level;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public static boolean isSymetric(TreeNode root) {
        if (root == null) {
            return true;
        }

        return isSymetric(root.left, root.right);
    }

    private static boolean isSymetric(TreeNode right, TreeNode left) {

        if (right == null || left == null) {
            return true;
        }
        System.out.println("rv " + right.val);
        System.out.println("lv " + left.val);
        System.out.println("r level " + right.level);
        System.out.println("l level " + left.level);

        if (right.val != left.val) {
            return false;
        }

        return isSymetric(right.right, left.right) && isSymetric(right.left, left.left);
    }

    public static void main(String[] args) {

        TreeNode root = new TreeNode(1);
        root.level = 0;
        root.left = new TreeNode(1);
        root.left.level = 1;
        root.right = new TreeNode(1);
        root.right.level = 1;
        root.right.left = new TreeNode(1);
        root.right.left.level = 2;
        root.right.right = new TreeNode(1);
        root.right.right.level = 2;
        System.out.println(isSymetric(root));
    }
}
