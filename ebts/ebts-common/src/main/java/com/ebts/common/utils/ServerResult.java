package com.ebts.common.utils;

/**
 * @author clay
 * @version 1.0
 * @mail 20932067@zju.edu.cn
 * @date 2020/12/15 21:34
 */
public class ServerResult<T> {
    private T data;// 成功时返回的数据
    private Integer count;
    private boolean start;
    private String msg;

    private String sheetName;

    public ServerResult() {
    }

    public ServerResult(boolean start, T data, Integer count) {
        this.data = data;
        this.count = count;
        this.start = start;
        this.msg = "操作成功";
    }

    public ServerResult(boolean start, T data) {
        this.data = data;
        this.start = start;
        this.msg = "操作成功";
    }

    public ServerResult(boolean start, T data, String sheetName) {
        this.data = data;
        this.start = start;
        this.sheetName = sheetName;
        this.msg = "操作成功";
    }

    public ServerResult(boolean start, String msg) {
        this.start = start;
        this.msg = msg;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public ServerResult(boolean start) {
        this.start = start;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
