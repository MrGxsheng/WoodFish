package com.woodfish.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author : hasd
 * @version 1.0.0
 * @date : 2023/1/8 20:22
 **/
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Result {
    private Integer code;
    private String msg;
    private Object Data;

    public static Result success() {
        return new Result(200, "请求成功", null);
    }

    public static Result success(String msg) {
        return new Result(200, msg, null);
    }

    public static Result success(Object data) {
        return new Result(200, "", data);
    }

    public static Result success(String msg, Object data) {
        return new Result(200, msg, data);
    }

    public static Result error() {
        return new Result(500, "遇到错误", "");
    }

    public static Result error(String msg) {
        return new Result(500, msg, "");
    }

    public static Result error(Integer code, String msg) {
        return new Result(code, msg, "");
    }
}
