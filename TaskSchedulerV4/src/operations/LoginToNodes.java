package operations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import framework.RestClientListener;
import gui.Progress;
import resources.Repository;
import resources.RestConfURL;

public class LoginToNodes 
{

	String url;
	String nodeName;
	Properties config;
	String configtext;
	public LoginToNodes(String nodeName)
	{
		this.nodeName = nodeName;
		if(!Repository.MapLogicalProtocolMap.containsKey(nodeName))
		{
		try {
		initialize();
		getNodeDetails();
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(null,"Unable to get the device details using the url :"
					+"\n" + url + "\n" + "Please click other device and proceed");
		}
		}
	}
	
	public LoginToNodes(String nodeName,String purpose)
	{
		this.nodeName = nodeName;
		if(!Repository.MapLogicalProtocolMap.containsKey(nodeName))
		{
		try {
		initialize1(nodeName);
		getNodeDetails();
		}catch(Exception e)
		{
			
		}
		}
	}
	
	public void initialize1(String purpose)
	{
		//construct url to connect to the controller and get the node ids
		if(purpose.contains("siae")|| purpose.contains("SIAE"))
		{
			loadProperties("LTP.config.SIAE");
		}else if(purpose.contains("ericsson") || purpose.contains("ERICSSON"))
		{
			loadProperties("LTP.config.Ericsson");
		}else if(purpose.contains("huawei") || purpose.contains("HUAWEI"))
		{
			loadProperties("LTP.config.Huawei");
		}else
		{
			loadProperties("LTP.config.ZTE");
		}
		url = RestConfURL.CONTROLLER_URL.getUrl() + RestConfURL.NODE_URL.getUrl().replace("<NODE_ID_OR_NAME>", nodeName) + RestConfURL.FILTER_LPID.getUrl().replace("<config>", configtext);
	}
	
	public void loadProperties(String vendor)
	{
		config = new Properties();
		try {
			FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/Config.properties");
			config.load(fis);
			
			configtext = config.getProperty(vendor);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initialize()
	{
		//construct url to connect to the controller and get the node ids
		url = RestConfURL.CONTROLLER_URL.getUrl() + RestConfURL.NODE_URL.getUrl().replace("<NODE_ID_OR_NAME>", nodeName) + RestConfURL.FILTER_LPID.getUrl().replace("<config>", "config");
	}
	
	public void getNodeDetails()
	{
		RestClientListener restCall = new RestClientListener(url);
		JSONObject jsonResponse = restCall.getResponse();
		processResponse(jsonResponse);
	}
	
	public void processResponse(JSONObject jsonResponse)
	{
		//getting core-model
        JSONObject coreModel  = (JSONObject)jsonResponse.get("core-model-1-4:control-construct");
        //getting LTPs
        JSONArray LTPDetails  = coreModel.getJSONArray("logical-termination-point");
        MapProtocolName(LTPDetails);
	}
	
	public void MapProtocolName(JSONArray LTPDetails)
    {
		Map<String,String> LogicalProtocolMap = new HashMap<String, String>();
		Map<String,String> LTP_LPMap = new HashMap<String, String>();
    	for(int i=0;i<LTPDetails.length();i++)
    	{
    		JSONObject json = LTPDetails.getJSONObject(i);
    		String uuid = json.getString("uuid");
    		JSONArray Protocol = json.getJSONArray("layer-protocol");
    		if(Protocol.getJSONObject(0).has("layer-protocol-name"))
    		{
    			String ProtocolName = Protocol.getJSONObject(0).getString("layer-protocol-name");    			
    			LogicalProtocolMap.put(uuid, ProtocolName);	
    		}else
    		{
    			LogicalProtocolMap.put(uuid, new String());
    		}
    		
    		if(Protocol.getJSONObject(0).has("local-id"))
    		{
    			String ProtocolName = Protocol.getJSONObject(0).getString("local-id");    			
    			LTP_LPMap.put(uuid, ProtocolName);	
    		}else
    		{
    			LTP_LPMap.put(uuid, new String());
    		}    		
    	}
    	Repository.MapLogicalProtocolMap.put(nodeName, LogicalProtocolMap);
    	Repository.MapLTP_LPMap.put(nodeName, LTP_LPMap);
    }
}
