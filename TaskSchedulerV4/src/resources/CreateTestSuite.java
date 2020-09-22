package resources;

import java.util.ArrayList;

public class CreateTestSuite {
	
	public String taskName;
	public String description;
	public String frequency;
	public String executionType;
	public ArrayList<CurrentTestSuite> testSuite;
	public static CreateTestSuite createTestSuite;
	
	public CreateTestSuite(String taskName,String description,String frequency,String executionType,
			ArrayList<CurrentTestSuite> testSuite)
	{
		this.taskName = taskName;
		this.description = description;
		this.frequency = frequency;
		this.executionType = executionType;
		this.testSuite = testSuite;				
	}

}
