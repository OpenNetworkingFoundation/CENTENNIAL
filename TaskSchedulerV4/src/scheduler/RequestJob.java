package scheduler;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import framework.ParallelRestCall;
import framework.SimpleRestCall;

public class RequestJob implements Job {

    private static Logger _log = LoggerFactory.getLogger(RequestJob.class);

     public RequestJob() {
    }

     public void execute(JobExecutionContext context)
        throws JobExecutionException {
       
       JobKey jobKey = context.getJobDetail().getKey();
       System.out.println("SimpleJob says: " + jobKey + " executing at " + new Date());
       SimpleRestCall restcall = new SimpleRestCall(jobKey.getName());
       restcall.performActions();
       
    }

}
