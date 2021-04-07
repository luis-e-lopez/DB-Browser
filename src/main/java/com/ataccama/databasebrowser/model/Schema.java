package com.ataccama.databasebrowser.model;

public class Schema {

    private String name;

    public Schema(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
