package com.ebts.quartz.util;

import org.quartz.JobExecutionContext;
import com.ebts.quartz.entity.Job;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author binlin
 */
public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, Job job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}
