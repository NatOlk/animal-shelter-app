package com.example.gr.test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetectLinkedListCycle {

    public static void main(String[] args) {
        ListNode ln1 = new ListNode(1);

        ListNode ln2 = new ListNode(2);
        ln1.next = ln2;

        ListNode ln3 = new ListNode(3);
        ln2.next = ln3;

        ListNode ln4 = new ListNode(4);
        ln3.next = ln4;

        ListNode ln5 = new ListNode(5);
        ln4.next = ln5;
        ln5.next = ln1;


        System.out.println(hasCycle(ln1));
    }

    public static boolean hasCycle(ListNode head) {

        ListNode h = head;
        List<ListNode> lln = new ArrayList<>();
        while (h != null) {
            System.out.println("H " + h.val);
            if (lln.contains(h)) return true;
            lln.add(h);
            h = h.next;
        }
        return false;
    }

    static class  ListNode {
      int val;
      ListNode next;
      ListNode(int x) {
          val = x;
          next = null;
      }
  }
}

