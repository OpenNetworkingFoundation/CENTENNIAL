package framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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
import org.json.JSONObject;

import gui.MainView;
import resources.CurrentTestSuite;
import resources.GetURL;
import resources.TaskDetails;

public class SimpleParallelRestCallThread implements Runnable{
	
	public JSONObject finalResult;
	public String taskid;
	public Date startTime;
	public Date endTime;
	
	public SimpleParallelRestCallThread(String taskid)
	{
		this.taskid = taskid;
	}
	
	public void run()
	{
		try
		{
		SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
	    String path = getUsersHomeDir() + File.separator + "ControllerTasks" + File.separator + taskid ;
	    String fileName = taskid + "_" + formatter.format(new Date());
	    File newFile = new File(path+ File.separator +fileName);
	    FileWriter myWriter = new FileWriter(newFile);
	    ArrayList<CurrentTestSuite> TestSuites = TaskDetails.taskRepo.get(taskid).testSuiteCollection;
	    Iterator<CurrentTestSuite> iterTestSuites = TestSuites.iterator();
	    int number = 0;
	    while(iterTestSuites.hasNext())
	    {
	    	CurrentTestSuite testSuite = iterTestSuites.next();	    	
	    	//"No", "URL Details", "Start Time", "End Time", "Status", "Latency"
	    	
	    	String urlTObeSent = GetURL.getURL(testSuite.testSuite, testSuite.type,testSuite.Details,testSuite.context);
	    	ParallelThread newThread = new ParallelThread(taskid,urlTObeSent,newFile,number,testSuite.Details,testSuite.testSuite,testSuite.context);
	    	newThread.start();	    	
	    	number++;
	    }	    
		
	    findandSet(MainView.tree,taskid,fileName);		
		((DefaultTreeModel)MainView.tree.getModel()).reload();
		//writeToMemoryLog(TestSuites,path,fileName);
		}catch(Exception e)
		{
			System.out.println("exception");
		}
		
	}
	
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	
	
	public ArrayList<String> getResponse(String url) {
        
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
        return output;
                        
        }
        catch(Exception ex)
        {
        	System.out.println(ex.toString());
        	return new ArrayList();
        }
        
    }
	
	
	public static String getUsersHomeDir() {
	    String users_home = System.getProperty("user.home");
	    return users_home.replace("\\", "/"); // to support all platforms.
	}
	
	private void findandSet(JTree tree, String folder,String fileName) {
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
    	DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
	    Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
	    while (e.hasMoreElements()) {
	        DefaultMutableTreeNode node = e.nextElement();
	        if (node.toString().equalsIgnoreCase(folder)) {
	        	DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(fileName);
	        	node.add(subNode);
	        }
	    }
	    model.reload(root);
	}
	
	private String milliSecToSeconds(long getDateDiff)
	{
		double sec = getDateDiff/1000;
		return Double.toString(sec);
	}
}
