package com.example.gr.test;

public class PrintAB {

    public static void main(String[] args) {
        SharedObject sharedObject = new SharedObject();

        Thread a = new Thread(() -> {
          {
               try {
                   sharedObject.printA();
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
       });

        Thread b = new Thread(() -> {
            try {
                sharedObject.printB();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        a.start();
        b.start();
    }

    private static class SharedObject {
        private boolean printA = true;

        public synchronized void printA() throws InterruptedException {
            for (int i = 0; i < 5; i++) {
                while (!printA) {
                    wait();
                }
                System.out.print("A");
                printA = false;
                notify();
            }
        }

        public synchronized void printB() throws InterruptedException {
            for (int i = 0; i < 5; i++) {
                while (printA) {
                    wait();
                }
                System.out.print("B");
                printA = true;
                notify();
            }
        }
    }
}

