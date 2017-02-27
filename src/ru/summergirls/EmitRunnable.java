package ru.summergirls;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

/**
 * Created by vanderer on 26.02.17.
 */
public class EmitRunnable implements Runnable
{
    private TaskPool pool;

    public EmitRunnable(TaskPool pool)
    {
        this.pool = pool;
    }

    public void run()
    {
        try {
            while (true) {
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

                try {
                    pool.add(new EventItem(dt, callable));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                System.out.println(Thread.currentThread().getName() + ", added");

                Thread.sleep((long)(Math.random() * 1000));
            }
        } catch (InterruptedException e) {}
    }

}
