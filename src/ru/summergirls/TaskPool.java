package ru.summergirls;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by vanderer on 26.02.17.
 */
public class TaskPool {

    protected ArrayList<EventItem> pool;

    public TaskPool()
    {
        pool = new ArrayList<EventItem>();
    }

    public void add(EventItem item)
    {
        pool.add(item);
    }

    public ArrayList<EventItem> popItems(LocalDateTime dt, int limit)
    {
        if (pool.size() == 0) {
            return new ArrayList<EventItem>();
        }

        Collections.sort(pool, new Comparator<EventItem>() {
            @Override
            public int compare(EventItem item1, EventItem item2)
            {
                return  item1.getDt().compareTo(item2.getDt());
            }
        });

        System.out.println("Pool: ");
        for (EventItem item : pool) {
            System.out.printf("%s\r\n", item.getDt().toString());
        }

        ArrayList<EventItem> items = new ArrayList<EventItem>();
        int n = 0;
        ArrayList<Integer> keysToRemove = new ArrayList<Integer>();
        for (int i = 0; i < pool.size(); i++) {
            EventItem item = pool.get(i);
            if (item.getDt().compareTo(dt) <= 0) {
                items.add(item);
                keysToRemove.add(i);
                if (n++ > limit) {
                    break;
                }
            }
        }

        if (keysToRemove.size() > 0) {
            Collections.reverse(keysToRemove);
            for (Integer key : keysToRemove) {
                try {
                    pool.get((int) key);
                } catch (IndexOutOfBoundsException e) {
                    System.out.printf("No such index: %d", key);
                    continue;
                }
                pool.remove((int) key);
            }
        }

        return items;
    }

}
