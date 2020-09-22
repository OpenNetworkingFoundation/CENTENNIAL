package framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

public class ParallelThread extends Thread {
	
	String url;
	File newFile;
	Date startTime;
	Date endTime;
	String taskid;
	int number;
	public static Map<String,Integer> executionStack = new HashMap<String,Integer>();
	String Details;
	String testSuite;
	String context;
	
	public ParallelThread(String taskid,String url,File newFile,int number,String Details,String testSuite,String context)
	{
		this.url = url;
		this.newFile = newFile;
		this.taskid = taskid;
		this.number = number;
		this.Details = Details;
		this.testSuite = testSuite;
		this.context = context;
	}
	
	
	
	public void run() {
        
		//connecting to the server and getting the first request
		ArrayList<String> output = new ArrayList<String>();
        try 
        {
		HttpHost targetHost = new HttpHost(ControllerConfig.IP, ControllerConfig.port, "http");
    	CredentialsProvider credsProvider = new BasicCredentialsProvider();
    	credsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(ControllerConfig.userName, ControllerConfig.password));
    	
    	//defining basic authentication
    	AuthCache authCache = new BasicAuthCache();
    	authCache.put(targetHost, new BasicScheme());
    	 
    	// Add AuthCache to the execution context
    	HttpClientContext context = HttpClientContext.create();
    	context.setCredentialsProvider(credsProvider);
    	context.setAuthCache(authCache);
    	HttpClient client = HttpClientBuilder.create().build();
    	HttpGet httpGet = new HttpGet(url);
    	
    	//new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
    	startTime = new Date();
        HttpResponse response = client.execute(httpGet,context);
        endTime = new Date();
        
        output.add(response.getStatusLine().toString());
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        StringBuilder builder = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null;) 
        {
           builder.append(line).append("\n");
        }
        output.add(builder.toString());
        WriteToOutput(output);                
        }
        catch(Exception ex)
        {
        	System.out.println(ex.toString());
        }
        
    }
	
	public synchronized void WriteToOutput(ArrayList<String> response)
	{
		try {
		FileWriter myWriter = new FileWriter(newFile.getAbsoluteFile(),true);
		myWriter.write(number+"----------"+ Details + "----------" + testSuite + "----------" + context
				+"----------"+
				url+"----------"+
    			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(startTime) + "----------" +
    			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(endTime) + "----------" +
    			response.get(0) + "----------" + getDateDiff(startTime,endTime,TimeUnit.MILLISECONDS) + "----------" + response.get(1));
		myWriter.close();
		}catch(Exception ex)
		{
			
		}
		executionStack.put(taskid, number);
	}

	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

}
