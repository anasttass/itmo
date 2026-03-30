package ru.stud.backend.model;

public enum ContextParams {
    POINT("point"),
    RESULTS("results"),
    ERROR("error"),
    REQUESTSTATS("requestStats"),
    X("X"),
    Y("Y"),
    R("R");

    private final String key;

    ContextParams(String key) {
        this.key = key;
    }

    public String getKey(){
        return key;
    }
}
