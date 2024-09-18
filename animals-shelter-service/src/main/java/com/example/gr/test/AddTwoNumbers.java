package com.example.gr.test;

public class AddTwoNumbers {

    public static void main(String[] args) {
        /*ListNode l13 = new ListNode(3);
        ListNode l12 = new ListNode(4, l13);
        ListNode l11 = new ListNode(2, l12);

        ListNode l23 = new ListNode(4);
        ListNode l22 = new ListNode(6, l23);
        ListNode l21 = new ListNode(5, l22);

*/
        ListNode l11 = new ListNode(9);

        ListNode l210 = new ListNode(9);
        ListNode l29 = new ListNode(9, l210);
        ListNode l28 = new ListNode(9, l29);
        ListNode l27 = new ListNode(9, l28);
        ListNode l26 = new ListNode(9, l27);
        ListNode l25 = new ListNode(9, l26);
        ListNode l24 = new ListNode(9, l25);
        ListNode l23 = new ListNode(9, l24);
        ListNode l22 = new ListNode(9, l23);
        ListNode l21 = new ListNode(1, l22);

        System.out.println("Result = " + addTwoNumbers(l11, l21));
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        return createListNode(getConvertedInt(l1) + getConvertedInt(l2));
    }

    private static long getConvertedInt(ListNode ln) {
        ListNode l1start = ln;
        StringBuilder sb1 = new StringBuilder();
        while(l1start != null) {
            sb1.insert(0, l1start.val);
            l1start = l1start.next;
        }
        return Long.valueOf(sb1.toString());
    }

    private static ListNode createListNode(long sum) {

        ListNode prevNd = null;
        ListNode currentNd = new ListNode();
        String sumNum = String.valueOf(sum);
        for (int i = 0; i < sumNum.length();  i++) {
           int num = sumNum.charAt(i) - '0';
           currentNd = new ListNode();
           currentNd.val = num;
           currentNd.next = prevNd;
           prevNd = currentNd;
        }

        return currentNd;
    }

    protected static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {
            return "ListNode{" +
                    "val=" + val +
                    ", next=" + next +
                    '}';
        }
    }
}
