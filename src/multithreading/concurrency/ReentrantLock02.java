package multithreading.concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class Worker implements Runnable {

    private final String name;
    private final ReentrantLock lock;

    public Worker(String name, ReentrantLock lock) {
        this.name = name;
        this.lock = lock;
    }

    @Override
    public void run() {

        try {
            lock.tryLock(2, TimeUnit.SECONDS);
            if (lock.isHeldByCurrentThread()) {
                System.out.printf("Thread %s pegou o lock%n", name);
            }
            System.out.printf("Thread %s entrou em uma sessão crítica%n", name);
            System.out.printf("%d Threads esperando na fila%n", lock.getQueueLength());
            System.out.printf("Thread %s vai esperar 2s%n", name);
            Thread.sleep(2000);
            System.out.printf("Thread %s finalizou a espera%n", name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

public class ReentrantLock02 {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock(true);
        new Thread(new Worker("A", lock)).start();
        new Thread(new Worker("B", lock)).start();
        new Thread(new Worker("C", lock)).start();
        new Thread(new Worker("D", lock)).start();
        new Thread(new Worker("E", lock)).start();
        new Thread(new Worker("F", lock)).start();
        new Thread(new Worker("G", lock)).start();
    }
}
