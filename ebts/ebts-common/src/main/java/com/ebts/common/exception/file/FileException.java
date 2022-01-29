package com.ebts.common.exception.file;

import com.ebts.common.exception.BaseException;

/**
 * 文件信息异常类
 *
 * @author binlin
 */
public class FileException extends BaseException {


    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }

}
