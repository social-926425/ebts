package com.ebts.common.exception.file;

/**
 * 文件名大小限制异常类
 *
 * @author binlin
 */
public class FileSizeLimitExceededException extends FileException {


    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("upload.exceed.maxSize", new Object[]{defaultMaxSize});
    }
}
