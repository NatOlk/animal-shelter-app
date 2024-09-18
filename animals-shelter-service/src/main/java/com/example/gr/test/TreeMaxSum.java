package com.example.gr.test;

public class TreeMaxSum {
      static int maxSum = Integer.MIN_VALUE;
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public static int maxPathSum(TreeNode root) {
        maxPathSumRec(root);
        return maxSum;
    }

    private static int maxPathSumRec(TreeNode node) {
        if (node == null) return 0;

        int leftSum = Math.max(0, maxPathSumRec(node.left));
        int rightSum = Math.max(0, maxPathSumRec(node.right));
        System.out.println("cur sum = " + leftSum);
        System.out.println("cur sum = " + rightSum);
        int currentSum = node.val + leftSum + rightSum;
        System.out.println("cur sum = " + currentSum);
        maxSum = Math.max(maxSum, currentSum);

        return node.val + Math.max(leftSum, rightSum);
    }

    public static void main(String[] args) {

        TreeNode root = new TreeNode(0);


        System.out.println(maxPathSum(root));
    }
}
