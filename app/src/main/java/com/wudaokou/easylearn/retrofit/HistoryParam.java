package com.wudaokou.easylearn.retrofit;

public class HistoryParam {
    public String course;
    public String name;
    public String uri;

    public HistoryParam(final String course, final String name) {
        this.course = course;
        this.name = name;
    }

    public HistoryParam(final String course, final String name, final String uri) {
        this.course = course;
        this.name = name;
        this.uri = uri;
    }
}
