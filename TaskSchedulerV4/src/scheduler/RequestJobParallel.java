package scheduler;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import framework.ParallelRestCall;

public class RequestJobParallel implements Job {

    private static Logger _log = LoggerFactory.getLogger(RequestJobParallel.class);

     public RequestJobParallel() {
    }

     public void execute(JobExecutionContext context)
        throws JobExecutionException {

       JobKey jobKey = context.getJobDetail().getKey();
       System.out.println("SimpleJob says: " + jobKey + " executing at " + new Date());
       ParallelRestCall restcall = new ParallelRestCall(jobKey.getName());
       restcall.performActions();
    }

}
