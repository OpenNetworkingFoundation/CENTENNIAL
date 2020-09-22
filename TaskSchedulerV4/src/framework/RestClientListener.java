package framework;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
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

public class RestClientListener {
	
	public JSONObject finalResult;
	public String url;
	
	public RestClientListener(String url)
	{
		this.url = url;
	}
	
	public JSONObject getResponse() {
        
		
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
    	
    	//connecting to the server and getting the first request
        try 
        {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet,context);
            System.out.println(response.getStatusLine());
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null;) 
            {
               builder.append(line).append("\n");
            }
            finalResult = new JSONObject(builder.toString());              
        }
        catch(Exception ex)
        {
        	System.out.println(ex.toString());
        }
        
        
        return finalResult;
    }
	
public String getStatus() {
        
		
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
    	
    	//connecting to the server and getting the first request
        try 
        {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet,context);
            return response.getStatusLine().toString();             
        }
        catch(Exception ex)
        {
        	return "failed";
        }
        
    }

}
