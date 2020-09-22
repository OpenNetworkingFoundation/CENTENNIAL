package resources;

import framework.ControllerConfig;

public enum RestConfURL {
	
	CONTROLLER_URL("http://<CONTROLLER_IP>:<CONTROLLER_PORT>/rests/data/network-topology:network-topology/topology=topology-netconf"),	
	FILTER_NODEID("?content=<config>&fields=node(node-id)"),
	NODE_URL("/node=<NODE_ID_OR_NAME>/yang-ext:mount/core-model-1-4:control-construct"),	
	FILTER_LPID("?content=<config>&fields=logical-termination-point(uuid;layer-protocol(layer-protocol-name;local-id))"),	
	LP_URL("/logical-termination-point=<LTP_ID>/layer-protocol=<LP_ID>/"),
	DEVICE_STATUS("?content=<config>&fields=node(node-id;netconf-node-topology:connection-status;netconf-node-topology:host;netconf-node-topology:port)"),

		
	AIR_INTERFACE_CAPABILITY("air-interface-2-0:air-interface-pac/air-interface-capability"),
	HYBRID_MICROWAVE_STRUCTURE_CAPABILITY("hybrid-mw-structure-2-0:hybrid-mw-structure-pac/hybrid-mw-structure-capability"),
	MAC_INTERFACE_CAPABILITY("mac-interface-1-0:mac-interface-pac/mac-interface-capability"),
	ETHERNET_CAPABILITY("ethernet-container-2-0:ethernet-container-pac/ethernet-container-capability"),
	PURE_ETHERNET_CAPABILITY("pure-ethernet-structure-2-0:pure-ethernet-structure-pac/pure-ethernet-structure-capability"),
	TDM_CAPABILITY("tdm-container-2-0:tdm-container-pac/tdm-container-capability"),
	WIRE_INTERFACE_CAPABILITY("wire-interface-2-0:wire-interface-pac/wire-interface-capability"),
	
	AIR_INTERFACE_CONFIGURATION("air-interface-2-0:air-interface-pac/air-interface-configuration"),
	HYBRID_MICROWAVE_STRUCTURE_CONFIGURATION("hybrid-mw-structure-2-0:hybrid-mw-structure-pac/hybrid-mw-structure-configuration"),
	MAC_INTERFACE_CONFIGURATION("mac-interface-1-0:mac-interface-pac/mac-interface-configuration"),
	ETHERNET_CONFIGURATION("ethernet-container-2-0:ethernet-container-pac/ethernet-container-configuration"),
	TDM_CONFIGURATION("tdm-container-2-0:tdm-container-pac/tdm-container-configuration"),
	WIRE_INTERFACE_CONFIGURATION("wire-interface-2-0:wire-interface-pac/wire-interface-configuration"),
	PURE_ETHERNET_CONFIGURATION("pure-ethernet-structure-2-0:pure-ethernet-structure-pac/pure-ethernet-structure-configuration");	
	
 
    private String url;
 
    RestConfURL(String envUrl) {
        this.url = envUrl;
    }
    RestConfURL() {
    }
 
    public String getUrl() {
    	if(url.contains("<CONTROLLER_IP>:<CONTROLLER_PORT>"))
    	{
    		String Connector = ControllerConfig.IP + ":" + ControllerConfig.port;
    		url = url.replace("<CONTROLLER_IP>:<CONTROLLER_PORT>", Connector);
    	}
        return url;
    }
       
	

}
