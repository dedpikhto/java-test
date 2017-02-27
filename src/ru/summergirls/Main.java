package ru.summergirls;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main
{
    final private static int THREADS = 3;
    final private static int PROCESSING_LIMIT = 10;

    public static void main(String[] args) {
        TaskPool pool = new TaskPool();

        ExecutorService service = Executors.newFixedThreadPool(THREADS);
        for (int i = 0; i < THREADS; i++) {
            service.submit(new EmitRunnable(pool));
        }

        Thread execute = new Thread(new Runnable() {
            public void run()
            {
                do {
                    LocalDateTime dt = LocalDateTime.now();

                    ArrayList<EventItem> items = pool.popItems(dt, PROCESSING_LIMIT);

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
                } while (true);
            }
        });
        execute.start();
    }
}
