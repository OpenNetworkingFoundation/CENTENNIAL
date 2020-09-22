package scheduler;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import resources.TaskDetails;

public class CustomScheduler implements LoadScheduler{
	
	  String taskNo = new String();
	  String executionType = new String();
	  public static SchedulerFactory sf = new StdSchedulerFactory();
	  public static Scheduler sched;	  
	  public static boolean isSchedulerInitiated = false;
		public static Map<String,JobDetail> SchedulerRepo = new HashMap<String,JobDetail>();
		public static Map<String,Integer> DurationRepo = new HashMap<String,Integer>();
		public static Map<String,Integer> Duration = new HashMap<String,Integer>();
	
	  public CustomScheduler(String taskNo,String executionType, String duration)
	  {
		  this.taskNo = taskNo;	
		  this.executionType = executionType;
		  Duration.put(taskNo,-1);
		  if(!duration.equals("none"))
		  {
		  Duration.put(taskNo,Integer.parseInt(duration));
	      }
		  DurationRepo.put(taskNo,0);
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
	    
		
		if(executionType.equalsIgnoreCase("Serial"))
		{
		    JobDetail job = newJob(RequestJob.class).withIdentity(taskNo, "group1").build();
			SchedulerRepo.put(taskNo, job);
		    String schedulerString = TaskDetails.taskRepo.get(taskNo).schedulerString;
		    CronTrigger trigger = newTrigger().withIdentity(taskNo, "group1").withSchedule(cronSchedule(schedulerString))
		        .build();
		    Date ft = sched.scheduleJob(job, trigger);
		    System.out.println(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
		             + trigger.getCronExpression());  
		    sched.start();
		}else
		{
			JobDetail job = newJob(RequestJobParallel.class).withIdentity(taskNo, "group1").build();			
			SchedulerRepo.put(taskNo, job);
		    String schedulerString = TaskDetails.taskRepo.get(taskNo).schedulerString;
		    CronTrigger trigger = newTrigger().withIdentity(taskNo, "group1").withSchedule(cronSchedule(schedulerString))
		        .build();
		    Date ft = sched.scheduleJob(job, trigger);
		    System.out.println(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
		             + trigger.getCronExpression());  
		    sched.start();
		}

	  }	  

	}