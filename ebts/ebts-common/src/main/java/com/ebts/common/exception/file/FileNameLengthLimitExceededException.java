package com.ebts.common.exception.file;

/**
 * 文件名称超长限制异常类
 *
 * @author binlin
 */
public class FileNameLengthLimitExceededException extends FileException {


    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("upload.filename.exceed.length", new Object[]{defaultFileNameLength});
    }
}
