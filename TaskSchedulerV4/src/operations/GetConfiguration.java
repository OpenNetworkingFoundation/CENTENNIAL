package operations;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import framework.RestClientListener;
import resources.Repository;
import resources.RestConfURL;

public class GetConfiguration 
{

	String url;
	String nodeName;
	String capacityType;
	String LTPID;
	public GetConfiguration(String nodeName,String LTPID)
	{
		this.nodeName = nodeName;
		this.LTPID = LTPID;
		initialize();
	}
	
	public void initialize()
	{
		//construct url to connect to the controller and get the node ids
		String LPID = Repository.LTP_LPMap.get(LTPID);		
		url = RestConfURL.CONTROLLER_URL.getUrl() + RestConfURL.NODE_URL.getUrl().replace("<NODE_ID_OR_NAME>", nodeName) + 
				RestConfURL.LP_URL.getUrl().replace("<LTP_ID>", LTPID).replace("<LP_ID>", LPID) + getConfiguration(LTPID);
	}
	
	public JSONObject getLTPDetails()
	{
		RestClientListener restCall = new RestClientListener(url);
		JSONObject jsonResponse = restCall.getResponse();
		return jsonResponse;
	}
	
	public Map processResponse(JSONObject jsonResponse)
	{
		//getting core-model
        JSONObject capacityObject  = (JSONObject)jsonResponse.get(getPacType(LTPID));        
        Map<String,String> mapping = MapAttributesToValues(capacityObject);
        return mapping;
	}
	
	public Map MapAttributesToValues(JSONObject capacityObject)
    {
		String[] attrubutes = capacityObject.getNames(capacityObject);
		Map<String,String> mapping = new HashMap();
		for(int i=0;i<attrubutes.length;i++)
		{
			if(capacityObject.get(attrubutes[i]) instanceof JSONArray)
			{
				JSONArray arr = capacityObject.getJSONArray(attrubutes[i]);
				mapping.put(attrubutes[i], arr.toString());
			}else if(capacityObject.get(attrubutes[i]) instanceof JSONObject)
			{
				JSONObject obj = capacityObject.getJSONObject(attrubutes[i]);
				mapping.put(attrubutes[i], obj.toString());
			}
			else if(capacityObject.get(attrubutes[i]) instanceof Integer)
			{
				String obj = Integer.toString(capacityObject.getInt(attrubutes[i]));
				mapping.put(attrubutes[i], obj.toString());
			}else if(capacityObject.get(attrubutes[i]) instanceof Boolean)
			{
				String obj = Boolean.toString(capacityObject.getBoolean(attrubutes[i]));
				mapping.put(attrubutes[i], obj.toString());
			}else if(capacityObject.get(attrubutes[i]) instanceof String)
			{
				String obj = capacityObject.getString(attrubutes[i]);
				mapping.put(attrubutes[i], obj.toString());
			}
		}
		return mapping;
    }
	
	public String getPacType(String LTPID)
	{
		if(Repository.LogicalProtocolMap.get(LTPID).contains("tdm-container"))
    	{
			return "tdm-container-2-0:tdm-container-configuration";
    	}else if(Repository.LogicalProtocolMap.get(LTPID).contains("ethernet-container"))
    	{
    		return "ethernet-container-2-0:ethernet-container-configuration";
    	}else if(Repository.LogicalProtocolMap.get(LTPID).contains("pure-ethernet-structure"))
    	{
    		return "pure-ethernet-structure-2-0:pure-ethernet-structure-configuration";
    	}else if(Repository.LogicalProtocolMap.get(LTPID).contains("hybrid-mw-structure"))
    	{
    		return "hybrid-mw-structure-2-0:hybrid-mw-structure-configuration";
    	}else if(Repository.LogicalProtocolMap.get(LTPID).contains("wire-interface"))
    	{
    		return "wire-interface-2-0:wire-interface-configuration";
    	}else if(Repository.LogicalProtocolMap.get(LTPID).contains("mac-interface"))
    	{
    		return "mac-interface-1-0:mac-interface-configuration";
    	}else
    	{
    		return "air-interface-2-0:air-interface-configuration";
    	}
		
	}
	
	public String getConfiguration(String LTPID)
	{
		if(Repository.LogicalProtocolMap.get(LTPID).contains("tdm-container"))
    	{
			return RestConfURL.TDM_CONFIGURATION.getUrl();
    	}else if(Repository.LogicalProtocolMap.get(LTPID).contains("ethernet-container"))
    	{
    		return RestConfURL.ETHERNET_CONFIGURATION.getUrl();
    	}else if(Repository.LogicalProtocolMap.get(LTPID).contains("pure-ethernet-structure"))
    	{
    		return RestConfURL.PURE_ETHERNET_CONFIGURATION.getUrl();
    	}else if(Repository.LogicalProtocolMap.get(LTPID).contains("hybrid-mw-structure"))
    	{
    		return RestConfURL.HYBRID_MICROWAVE_STRUCTURE_CONFIGURATION.getUrl();
    	}else if(Repository.LogicalProtocolMap.get(LTPID).contains("wire-interface"))
    	{
    		return RestConfURL.WIRE_INTERFACE_CONFIGURATION.getUrl();
    	}else if(Repository.LogicalProtocolMap.get(LTPID).contains("mac-interface"))
    	{
    		return RestConfURL.MAC_INTERFACE_CONFIGURATION.getUrl();
    	}else
    	{
    		return RestConfURL.AIR_INTERFACE_CONFIGURATION.getUrl();
    	}
		
	}
}
