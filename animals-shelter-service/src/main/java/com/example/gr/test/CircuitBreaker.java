package com.example.gr.test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class CircuitBreaker {
    private final int failureThreshold;
    private final long recoveryTimeout;
    private int failureCount;
    private State state;
    private long lastFailureTime;

    private enum State {
        CLOSED,
        OPEN,
        HALF_OPEN
    }

    public CircuitBreaker(int failureThreshold, long recoveryTimeout) {
        this.failureThreshold = failureThreshold;
        this.recoveryTimeout = recoveryTimeout;
        this.failureCount = 0;
        this.state = State.CLOSED;
    }

    public <T> T call(Callable<T> callable) throws Exception {
        switch (state) {
            case OPEN:
                if (System.currentTimeMillis() - lastFailureTime > recoveryTimeout) {
                    state = State.HALF_OPEN;
                } else {
                    throw new Exception("Circuit is open");
                }
                break;
            case HALF_OPEN:
                // Try the operation and see if it succeeds
                break;
            case CLOSED:
                // Proceed with the normal operation
                break;
        }

        try {
            T result = callable.call();
            onSuccess();
            return result;
        } catch (Exception e) {
            onFailure();
            throw e;
        }
    }

    private void onSuccess() {
        failureCount = 0;
        state = State.CLOSED;
    }

    private void onFailure() {
        failureCount++;
        if (failureCount >= failureThreshold) {
            state = State.OPEN;
            lastFailureTime = System.currentTimeMillis();
        }
    }

    public static void main(String[] args) {
        CircuitBreaker circuitBreaker = new CircuitBreaker(3, TimeUnit.SECONDS.toMillis(5));

        Callable<String> callable = () -> {
            // Simulate a task that can fail
            if (Math.random() > 0.5) {
                throw new RuntimeException("Task failed");
            }
            return "Task succeeded";
        };

        for (int i = 0; i < 10; i++) {
            try {
                String result = circuitBreaker.call(callable);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                Thread.sleep(1000); // Sleep for a while before the next call
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
