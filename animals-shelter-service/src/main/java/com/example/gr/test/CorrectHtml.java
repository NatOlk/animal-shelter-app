package com.example.gr.test;


import java.util.ArrayDeque;
import java.util.Deque;

public class CorrectHtml {

    public static void main(String[] args) {
        String html = "<div>Blaaaa</div>";

        System.out.println(correctHtml(html));
    }

    private static boolean correctHtml (String html) {
        Deque<String> stackHtml = new ArrayDeque<>();
        int index = 0;
        while(true) {
            int indexOpen = html.indexOf("<", index);
            if (indexOpen == -1) break;
            index = html.indexOf(">", indexOpen);

            String tag = html.substring(indexOpen, index + 1);
            stackHtml.add(tag);
        }

        System.out.println(stackHtml);
        while(!stackHtml.isEmpty()) {
            String first = stackHtml.pollFirst();
            String last = stackHtml.pollLast();
            if (first == null || last == null) return false;
            StringBuilder lastBl = new StringBuilder(last);
            lastBl.delete(1, 2);

            if(!first.contentEquals(lastBl))
            {
                return false;
            }
        }
        return true;
    }
}

