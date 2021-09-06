package com.wudaokou.easylearn.retrofit;

public class Answer {
    String all;
    String fsanswer;
    String subject;
    String  message;
    String tamplateContent;
    Integer fs;
    String filterStr;
    String subjectUri;
    String predicate;
    Double score;
    Boolean answerflag;
    String attention;
    String fsscore;
    String value;

    public Answer(String all, String subject, String fsanswer, String message, String tamplateContent, Integer fs, String filterStr,
                  String subjectUri, String predicate, Double score, Boolean answerflag, String attention, String fsscore, String value){
        this.all = all;
        this.subject = subject;
        this.fsanswer = fsanswer;
        this.message = message;
        this.tamplateContent = tamplateContent;
        this.fs = fs;
        this.filterStr = filterStr;
        this.subjectUri = subjectUri;
        this.predicate = predicate;
        this.score = score;
        this.answerflag = answerflag;
        this.attention = attention;
        this.fsscore = fsscore;
        this.value = value;
    }

    public Double getScore(){
        return score;
    }

    public String getValue(){
        return value;
    }
}
