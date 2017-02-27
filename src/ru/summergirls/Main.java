package ru.summergirls;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Main
{
    final private static int THREADS = 10;
    final private static int PROCESSING_LIMIT = 10;

    public static void main(String[] args) {
        TaskPool pool = new TaskPool();
        Semaphore sem = new Semaphore(1);

        ExecutorService service = Executors.newFixedThreadPool(THREADS);
        for (int i = 0; i < THREADS; i++) {
            service.submit(new EmitRunnable(pool, sem));
        }

        Thread execute = new Thread(new Runnable() {
            public void run()
            {
                while (!Thread.currentThread().isInterrupted()) {
                    LocalDateTime dt = LocalDateTime.now();

                    try {
                        sem.acquire();
                    } catch (InterruptedException e) {}

                    ArrayList<EventItem> items = pool.popItems(dt, PROCESSING_LIMIT);

                    sem.release();

                    if (items.size() > 0) {
                        System.out.printf("Processing %d items at %s\r\n", items.size(), dt.toString());

                        ExecutorService service = Executors.newFixedThreadPool(items.size());
                        for (EventItem item : items) {
                            Future<String> result = service.submit(item.getCallable());
                            try {
                                System.out.printf("Callable result: %s\r\n", result.get());
                            } catch (ExecutionException | InterruptedException e) {}
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
            }
        });
        execute.start();
    }
}
