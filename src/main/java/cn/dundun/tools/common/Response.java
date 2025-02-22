package cn.dundun.tools.common;

import lombok.Data;

/**
 * 统一响应类
 *
 * @author xiaotian
 * @since 2025/2/13 15:26
 */
@Data
public class Response<T> {

    private int code;

    private String msg;

    private T data;

    /**
     * 成功响应
     *
     * @param data 响应内容
     * @return 响应
     */
    public static <T> Response<T> success(T data) {

        Response<T> response = new Response<>();

        response.code = 200;
        response.msg = "success";
        response.data = data;

        return response;
    }

    /**
     * 失败响应
     *
     * @param msg 失败信息
     * @return 响应
     */
    public static <T> Response<T> fail(String msg) {

        Response<T> response = new Response<>();

        response.code = 500;
        response.msg = msg;

        return response;
    }
}
