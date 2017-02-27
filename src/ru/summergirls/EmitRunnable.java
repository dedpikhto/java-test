package ru.summergirls;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

/**
 * Created by vanderer on 26.02.17.
 */
public class EmitRunnable implements Runnable
{
    private TaskPool pool;
    private Semaphore sem;

    public EmitRunnable(TaskPool pool, Semaphore sem)
    {
        this.pool = pool;
        this.sem = sem;
    }

    public void run()
    {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                long secondsDelta = (long)(Math.random() * 1000);

                LocalDateTime dt = LocalDateTime.now();

                if (Math.random() > .5) {
                    dt = dt.plusSeconds(secondsDelta);
                } else {
                    dt = dt.minusSeconds(secondsDelta);
                }

                String callableResult = dt.toString();

                Callable<String> callable = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return callableResult;
                    }
                };

                sem.acquire();

                try {
                    pool.add(new EventItem(dt, callable));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                sem.release();

                System.out.println(Thread.currentThread().getName() + ", added");

                Thread.sleep((long)(Math.random() * 1000));
            }
        } catch (InterruptedException e) {}
    }

}
