package producerconsumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProducerConsumer {

    private static volatile BlockingQueue<Long> s1 = new ArrayBlockingQueue(11);
    private static volatile BlockingQueue<Long> s2 = new ArrayBlockingQueue(11);

    public static void main(String[] args) {
//        CREATE BLOCKINGQUEUE AND FILL IT
        s1.add(4L);
        s1.add(5L);
        s1.add(8L);
        s1.add(12L);
        s1.add(21L);
        s1.add(22L);
        s1.add(34L);
        s1.add(35L);
        s1.add(36L);
        s1.add(37L);
        s1.add(42L);

        runWithThread(10);
        
    }
    
    public static void runWithThread(int noThreads) {
        ArrayList<Thread> threadList = new ArrayList();
        
        for (int i = 0; i < noThreads; i++) {
            threadList.add(new myThread());
        }
        
        double begin = System.nanoTime();
        
        for (Thread t : threadList) {
            t.start();
        }
        

//        CONSUMER THREAD
        Consumer cons = new Consumer();
        cons.setName("Consumer");
        Thread c1 = new Thread(cons);
        c1.start();
        
        for (Thread t1 : threadList) {
            try {
                t1.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ProducerConsumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        
        System.out.println("The sum of all numbers are: " + cons.getSum());
        double end = System.nanoTime();
        System.out.println("Time spend with: " + noThreads + " thread, was: " + (end - begin));
        c1.stop();
        
    }

//    NEW THREAD CLASS
    public static class myThread extends Thread {

        @Override
        public void run() {
            try {
                while (s1.peek() != null) {
                    long result = fib(s1.poll());
                    s2.put(result);
                }

            } catch (InterruptedException ex) {
                Logger.getLogger(ProducerConsumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private long fib(long n) {
            if ((n == 0) || (n == 1)) {
                return n;
            } else {
                return fib(n - 1) + fib(n - 2);
            }
        }

    }

    public static class Consumer implements Runnable {

        private String name;
        private long sum, number;

        @Override
        public void run() {
            while (true) {
                try {
                    number = s2.take();
                    System.out.println("Number added: " + number);
                    sum += number;
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProducerConsumer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public long getSum() {
            return sum;
        }

    }

}
