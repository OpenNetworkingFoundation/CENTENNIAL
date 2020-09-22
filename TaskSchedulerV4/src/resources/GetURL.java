package resources;

import javax.swing.JCheckBox;

public class GetURL {
	
	public static String getURL(String request,String type,String nodeName,String content)
	{
		if(type.equals("Controller"))
		{
			if(request.equals("Get node mount status"))
			{
				
				String url = RestConfURL.CONTROLLER_URL.getUrl() + RestConfURL.DEVICE_STATUS.getUrl().replace("<config>", content);
				if(content.equals("none"))
				{
					url = url.replace("content=none&","");
				}
				return url;
			}
			else if(request.equals("Get Node details"))
			{
				
				String url = RestConfURL.CONTROLLER_URL.getUrl() + RestConfURL.FILTER_NODEID.getUrl().replace("<config>", content);
				if(content.equals("none"))
				{
					url = url.replace("content=none&","");
				}
				return url;
			}
		}else if(type.equals("Node"))
		{
			if(request.equals("Get Device Capabilities"))
			{
				String url =  RestConfURL.CONTROLLER_URL.getUrl() + "/node=" +nodeName;
				if(!content.equals("none"))
				{
					url = url + "?content=" + content;
				}
				return url;
			}else if(request.equals("Get LTP Details"))
			{
				
				String url =  RestConfURL.CONTROLLER_URL.getUrl() + RestConfURL.NODE_URL.getUrl().replace("<NODE_ID_OR_NAME>",nodeName) + RestConfURL.FILTER_LPID.getUrl().replace("<config>", content);
				if(content.equals("none"))
				{
					url = url.replace("content=none&","");
				}
				return url;
			}else if(request.equals("Get Device control-construct"))
			{
				String url =  RestConfURL.CONTROLLER_URL.getUrl() + RestConfURL.NODE_URL.getUrl().replace("<NODE_ID_OR_NAME>",nodeName);
				if(!content.equals("none"))
				{
					url = url + "?content=" + content;
				}
				return url;
			}		
			//http://10.118.125.76:8181/rests/data/network-topology:network-topology/topology=topology-netconf/node=ericsson_trafficnode_06251/yang-ext:mount/core-model-1-4:control-construct
		}else
		{
			String nodeID = nodeName.split(":")[0];
			String LTPID = nodeName.split(":")[1];
			String LPID = Repository.MapLTP_LPMap.get(nodeID).get(LTPID);
			if(request.equals("Get AirInterface Capability"))
			{
				String url = RestConfURL.CONTROLLER_URL.getUrl() + RestConfURL.NODE_URL.getUrl().replace("<NODE_ID_OR_NAME>", nodeID) + 
				RestConfURL.LP_URL.getUrl().replace("<LTP_ID>", LTPID).replace("<LP_ID>", LPID) + RestConfURL.AIR_INTERFACE_CAPABILITY.getUrl();
				if(!content.equals("none"))
				{
					url = url + "?content=" + content;					
				}
				return url;
			}else if(request.equals("Get AirInterface Configuration"))
			{
				String url = RestConfURL.CONTROLLER_URL.getUrl() + RestConfURL.NODE_URL.getUrl().replace("<NODE_ID_OR_NAME>", nodeID) + 
						RestConfURL.LP_URL.getUrl().replace("<LTP_ID>", LTPID).replace("<LP_ID>", LPID) + RestConfURL.AIR_INTERFACE_CONFIGURATION.getUrl();
				if(!content.equals("none"))
				{
					url = url + "?content=" + content;
				}
				return url;
			}else
			{
				String url = RestConfURL.CONTROLLER_URL.getUrl() + RestConfURL.NODE_URL.getUrl().replace("<NODE_ID_OR_NAME>", nodeID) + 
						RestConfURL.LP_URL.getUrl().replace("<LTP_ID>", LTPID).replace("<LP_ID>", LPID) + RestConfURL.AIR_INTERFACE_CONFIGURATION.getUrl()+"/"
						+request.split(" ")[2];
				if(!content.equals("none"))
				{
					url = url + "?content=" + content;
				}
				return url;
			}
		}
		
		return null;
	}

}
