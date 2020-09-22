package scheduler;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import resources.TaskDetails;

public class CustomSchedulerParallel {
	
	  String taskNo = new String();
	  public static SchedulerFactory sf = new StdSchedulerFactory();
	  public static Scheduler sched;	  
	  public static boolean isSchedulerInitiated = false;
	
	  public CustomSchedulerParallel(String taskNo)
	  {
		  this.taskNo = taskNo;		  
		  if(!isSchedulerInitiated)
		  {
			  try
			  {
				  sched = sf.getScheduler();
				  isSchedulerInitiated = true;
			  }catch(Exception ex)
			  {
				  
			  }
		  }
	  }

	  public void run() throws Exception {		
	    
	    JobDetail job = newJob(RequestJobParallel.class).withIdentity(taskNo, "group1").build();
	    String schedulerString = TaskDetails.taskRepo.get(taskNo).schedulerString;
	    CronTrigger trigger = newTrigger().withIdentity(taskNo, "group1").withSchedule(cronSchedule(schedulerString))
	        .build();
	    Date ft = sched.scheduleJob(job, trigger);
	    System.out.println(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
	             + trigger.getCronExpression());  
	    sched.start();

	  }	  

	}