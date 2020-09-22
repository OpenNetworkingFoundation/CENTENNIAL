package resources;

import java.util.ArrayList;

public class CurrentTestSuite {
	
	public static ArrayList<CurrentTestSuite> CurrentList = new ArrayList<CurrentTestSuite>();
	public String no;
	public String type;
	public String testSuite;
	public String Details;
	public String context;
	
	public CurrentTestSuite(String no,String type,String testSuite,String Details,String context)
	{
		this.no = no;
		this.type = type;
		this.testSuite = testSuite;
		this.Details = Details;
		this.context = context;
	}
	
	
}
