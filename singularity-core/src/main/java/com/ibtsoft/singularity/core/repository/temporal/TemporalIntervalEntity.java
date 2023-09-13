package com.ibtsoft.singularity.core.repository.temporal;

import java.time.temporal.Temporal;

public class TemporalIntervalEntity<T extends Temporal & Comparable<T>> extends TemporalEntity<T> {

    private T start;
    private T end;

    public T getStart() {
        return start;
    }

    public void setStart(final T start) {
        this.start = start;
    }

    public T getEnd() {
        return end;
    }

    public void setEnd(final T end) {
        this.end = end;
    }
}
