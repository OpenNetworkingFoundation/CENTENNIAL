package resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import scheduler.CustomScheduler;
import scheduler.LoadScheduler;

public class TaskDetails {
	
	public String taskName = new String();
	public String description = new String();
	public ArrayList<CurrentTestSuite> testSuiteCollection = null;
	public String schedulerString = new String();
	public String Executiontype = new String();
	public static Map<String,TaskDetails> taskRepo = new HashMap<String,TaskDetails>();
	public String duration = new String();
	
	public TaskDetails(String taskName,String description,ArrayList<CurrentTestSuite> testSuiteCollection,String Executiontype,String frequency,String duration)
	{
		this.taskName = taskName;
		this.description = description;
		this.testSuiteCollection = testSuiteCollection;
		this.Executiontype = Executiontype;
		this.schedulerString = formulateScheduler(frequency);		
		this.duration = duration;
	}
	
	//{"15 mins", "30 mins", "1 Hr", "3 Hrs", "6 Hrs", "12 Hrs", "24 Hrs"}
	//"0 0/3 * * * ?"
	public String formulateScheduler(String frequency)
	{
		
	    if(frequency.equals("0-0-0"))
	    {
	    	return "One Time";
	    }
	    else
	    {
		    String[] time = frequency.split("-");
			String hours = time[0];
			String mins = time[1];
			String secs = time[2];
			
			if(mins.equals("0") && hours.equals("0"))
			{
				return "0/" + secs + " * * * * ?";
			}
			else if(secs.equals("0") && hours.equals("0"))
			{
				return "0 0/"+ mins +" * * * ?";
			}
			else if(secs.equals("0") && mins.equals("0"))
			{
				return "0 0 0/" +hours+" * * ?";
			}
			else if(!secs.equals("0") && !mins.equals("0") && hours.equals("0"))
			{
				return secs +" 0/"+ mins +" * * * ?";				
			}
			else if(secs.equals("0") && !mins.equals("0") && !hours.equals("0"))
			{
				return "0 "+ mins +" 0/" +hours+" * * ?";
			}
			else if(!secs.equals("0") && mins.equals("0") && !hours.equals("0"))
			{
				return secs +" 0 0/" +hours+" * * ?";
			}
			else
			{
				return secs +" "+ mins +" 0/" +hours+" * * ?";
			}			
		
	    }
		
	}
	
		

}
