package com.ataccama.databasebrowser.model;

public class ColumnStats {

    private String max;
    private String min;
    private String avg;
    private String median;

    public ColumnStats(String max, String min, String avg, String median) {
        this.max = max;
        this.min = min;
        this.avg = avg;
        this.median = median;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getMedian() {
        return median;
    }

    public void setMedian(String median) {
        this.median = median;
    }
}
