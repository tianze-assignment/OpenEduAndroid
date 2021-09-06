package com.wudaokou.easylearn.constant;

public class Constant {
    //public static final String eduKGQuestionUrl = "http://open.edukg.cn/opedukg/api/typeOpen/open/inputQuestion";
    public static final String eduKGBaseUrl = "http://open.edukg.cn/opedukg/api/typeOpen/open/";
    public static final String eduKGLoginUrl = "http://open.edukg.cn/opedukg/api/typeAuth/";
    public static final String eduKGPhone = "18744798635";
    public static final String eduKGPassword = "12345678wtz";
    public static String eduKGId = "";

    public static final String backendBaseUrl = "http://tianze.site:6789";

    public static String backendToken = "";

    public static final String[] subjectList = {"chinese", "math", "english",
            "physics", "chemistry", "biology", "history",
            "geo", "politics"};

    // 用于分割选择题选项
    public static final String[] choiceSplitChars = {
            ".", "、", "．", ""
    };
}
