package com.ksondzyk.Processing;

import com.ksondzyk.Server;
import lombok.Data;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProcessingQueue implements Runnable {

    static ExecutorService executorPool = Executors.newFixedThreadPool(Server.processingThreadCount);
    static BlockingQueue<ProcessingQueue.Task> concurrentLinkedQueue = new LinkedBlockingQueue<>();

    public static void runProcessing() {
        for(int i = 0; i < Server.processingThreadCount; i++)
            executorPool.submit(new ProcessingQueue());
    }

    public static Task process(int id) {
        ProcessingQueue.Task processingQueueTask = new ProcessingQueue.Task(id);
        concurrentLinkedQueue.add(processingQueueTask);
        return processingQueueTask;
    }

    @Data
    public static class Task {
        Integer id;
        AtomicBoolean isDone = new AtomicBoolean(false);
        String result;

        public Boolean isDone() {
            return this.isDone.get();
        }

        Task (Integer id) { this.id = id; }
    }

    @Override
    public void run() {
        while (Server.serverIsWorking) {
            Task task;
            do {
                task = concurrentLinkedQueue.poll();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (Server.serverIsWorking && task == null);
            if (task != null) {
                int id = task.getId();
                try {
                    Thread.sleep(1000 * Server.secondsPerTask);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.setResult("it was id " + id);
                task.getIsDone().set(true);
            }
        }
    }

    public static void shutdown() {
        executorPool.shutdown();
        try {
            if (!executorPool.awaitTermination(60, TimeUnit.SECONDS))
                System.err.println("ProcessingQueue threads didn't finish in 60 seconds!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}