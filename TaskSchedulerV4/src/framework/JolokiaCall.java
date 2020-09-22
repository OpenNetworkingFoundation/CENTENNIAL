package framework;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

public class JolokiaCall 
{
	//public static String url = "http://"+ ControllerConfig.IP +":" + ControllerConfig.port +"/jolokia/read/java.lang:type=Memory/HeapMemoryUsage";
	public static String url = "http://"+ ControllerConfig.IP +":" + 8181 +"/jolokia/read/java.lang:type=Memory/HeapMemoryUsage";
	Properties config;
	public static String jolokia_username;
	public static String jolokia_password;
	/*
	 * public static void main(String[] args) { new
	 * JolokiaCall().getJolokiaResponse(); }
	 */
	
	public JolokiaCall()
	{
		loadProperties();
	}

	public Map<String,String> getJolokiaResponse()
	{
		Map<String,String> map = new HashMap();
		try
		{
			//Rest Call
			//HttpHost targetHost = new HttpHost(ControllerConfig.IP, ControllerConfig.port, "http");
			HttpHost targetHost = new HttpHost(ControllerConfig.IP, 8181, "http");			
	    	CredentialsProvider credsProvider = new BasicCredentialsProvider();
	    	credsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(jolokia_username, jolokia_password));
	    	
	    	//defining basic authentication
	    	AuthCache authCache = new BasicAuthCache();
	    	authCache.put(targetHost, new BasicScheme());
	    	 
	    	// Add AuthCache to the execution context
	    	HttpClientContext context = HttpClientContext.create();
	    	context.setCredentialsProvider(credsProvider);
	    	context.setAuthCache(authCache);
	    	HttpClient client = HttpClientBuilder.create().build();
	    	HttpGet httpGet = new HttpGet(url);
	    	
	    	//get the response
	    	HttpResponse response = client.execute(httpGet,context);
	    	String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	    	//Parsing output
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null;) 
            {
               builder.append(line).append("\n");
            }
	    	JSONObject finalResult = new JSONObject(builder.toString());
	    	long max = finalResult.getJSONObject("value").getLong("max");
	    	long used = finalResult.getJSONObject("value").getLong("used");
	    	map.put("timeStamp", dateTime);
	    	map.put("max", Long.toString(max));
	    	map.put("used", Long.toString(used));
	    	System.out.println(builder.toString());
	    	
		//adding available space , timestamp and used space to the arraylist
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
		}
		return map;
	}
	
	
	public void loadProperties()
	{
		config = new Properties();
		try {
			FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/Config.properties");
			config.load(fis);
			
			 jolokia_username = config.getProperty("Controller.jolokia.username");
			 jolokia_password= config.getProperty("Controller.jolokia.password");
			 
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
