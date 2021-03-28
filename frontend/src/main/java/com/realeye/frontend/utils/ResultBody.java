package com.realeye.frontend.utils;

import lombok.Data;

@Data
public class ResultBody {

    private int code;

    private String message;

    private Object data;

    private ResultBody() {
    }

    private ResultBody(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResultBody newSuccessInstance() {
        return new ResultBody(200, "提交成功", null);
    }

    public static ResultBody newSuccessInstance(Object o) {
        return new ResultBody(200, "提交成功", o);
    }

    public static ResultBody newErrorInstance() {
        return new ResultBody(403, "处理失败", null);
    }

    public static ResultBody newErrorInstance(int code, String message) {
        return new ResultBody(code, message, null);
    }
}
