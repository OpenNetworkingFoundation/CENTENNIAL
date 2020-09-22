package operations;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import framework.RestClientListener;
import resources.Repository;
import resources.RestConfURL;

public class LoginToController {
	
	String url;
	public LoginToController()
	{
		initialize();
	}
	
	public void initialize()
	{
		//construct url to connect to the controller and get the node ids
		url = RestConfURL.CONTROLLER_URL.getUrl() + RestConfURL.FILTER_NODEID.getUrl().replace("<config>", "config");
		System.out.println("login :" + url);
	}
	
	public JSONObject getNodeDetails()
	{
		RestClientListener restCall = new RestClientListener(url);
		JSONObject jsonResponse = restCall.getResponse();
		return jsonResponse;
	}
	
	public void processResponse(JSONObject jsonResponse)
	{
		try
		{
		JSONArray topologyDetails  = jsonResponse.getJSONArray("network-topology:topology");
        JSONObject NodeDetails = topologyDetails.getJSONObject(0);        
        JSONArray NodeArray = NodeDetails.getJSONArray("node");
        for(int i=0;i<NodeArray.length();i++)
    	{
    		JSONObject json = NodeArray.getJSONObject(i);
    		String nodeID = json.getString("node-id");
    		Repository.NodeList.add(nodeID);    		
    	}
		}catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, "something went wrong.. unable to connect to the Controller");
		}
	}
	
	public String checkStatus()
	{
		RestClientListener restCall = new RestClientListener(url);
		return restCall.getStatus();
	}

}
