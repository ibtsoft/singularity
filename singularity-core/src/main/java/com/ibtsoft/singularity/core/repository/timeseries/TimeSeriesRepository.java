package com.ibtsoft.singularity.core.repository.timeseries;

import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;

public class TimeSeriesRepository<T extends Temporal & Comparable<T>, I, V> {

    private Map<I, Map<T, V>> timeSeries = new HashMap<>();
}
