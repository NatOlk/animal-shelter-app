package com.example.gr.test;

public class AddTwoNumbers2 {

    public static void main(String[] args) {
        /*ListNode l13 = new ListNode(3);
        ListNode l12 = new ListNode(4, l13);
        ListNode l11 = new ListNode(2, l12);

        ListNode l23 = new ListNode(4);
        ListNode l22 = new ListNode(6, l23);
        ListNode l21 = new ListNode(5, l22);*/


        ListNode l13 = new ListNode(9);
        ListNode l12 = new ListNode(9, l13);
        ListNode l11 = new ListNode(9, l12);


        ListNode l27 = new ListNode(9);
        ListNode l26 = new ListNode(9, l27);
        ListNode l25 = new ListNode(9, l26);
        ListNode l24 = new ListNode(9, l25);
        ListNode l23 = new ListNode(9, l24);
        ListNode l22 = new ListNode(9, l23);
        ListNode l21 = new ListNode(9, l22);

        System.out.println("Result = " + addTwoNumbers(l11, l21));
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        ListNode prevNd = null;
        ListNode currentNd = null;

        ListNode l1start = l1;
        ListNode l2start = l2;
        int tens = 0;
        int units = 0;
        while (l1start != null || l2start != null) {
            int fn = l1start != null ? l1start.val : 0;
            int sn = l2start != null ? l2start.val : 0;

            int sum = fn + sn + tens;
            tens = sum / 10;
            units = sum % 10;

            currentNd = new ListNode();
            currentNd.val = units;
            currentNd.next = prevNd;
            prevNd = currentNd;
            l1start = l1start != null ? l1start.next : null;
            l2start = l2start != null ? l2start.next : null;

        }
        if (tens == 1) {
            ListNode additonalNd = new ListNode();
            additonalNd.val = tens;
            additonalNd.next = currentNd;
            currentNd = additonalNd;
        }
        return convert(currentNd);
    }

    private static ListNode convert(ListNode curNd) {

        ListNode ln = curNd;
        ListNode newNd = null;
        ListNode previousNd = null;

        while (ln != null) {
            newNd = new ListNode();
            newNd.val = ln.val;

            newNd.next = previousNd;
            previousNd = newNd;
            ln = ln.next;
        }
        return newNd;
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
