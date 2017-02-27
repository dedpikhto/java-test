package ru.summergirls;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

/**
 * Created by vanderer on 26.02.17.
 */
public class EventItem {

    protected LocalDateTime dt;
    protected Callable<String> callable;

    public EventItem(LocalDateTime dt, Callable<String> callable)
    {
        this.dt = dt;
        this.callable = callable;
    }

    public LocalDateTime getDt()
    {
        return dt;
    }

    public Callable<String> getCallable()
    {
        return callable;
    }

}
