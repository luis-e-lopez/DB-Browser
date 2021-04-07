package com.ataccama.databasebrowser.model;

public class TableStats {

    private long columnsCount;
    private long recordsCount;

    public TableStats(long columnsCount, long recordsCount) {
        this.columnsCount = columnsCount;
        this.recordsCount = recordsCount;
    }

    public long getColumnsCount() {
        return columnsCount;
    }

    public void setColumnsCount(long columnsCount) {
        this.columnsCount = columnsCount;
    }

    public long getRecordsCount() {
        return recordsCount;
    }

    public void setRecordsCount(long recordsCount) {
        this.recordsCount = recordsCount;
    }
}
