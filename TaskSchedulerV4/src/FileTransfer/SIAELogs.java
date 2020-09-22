package FileTransfer;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


public class SIAELogs extends Thread {

	/**
	 * ssh connect to controller
	 * 
	 */
	File newFile;
	public SIAELogs(File newFile)
	{
	this.newFile = 	newFile;
	}
	
	public void run() {
		
	    String host="172.29.145.45";
	    String user="siaemed";
	    String password="siae1952";
	    String command1="cd /opt/mediation_device/netconfd/33002/;tail -f karaf.log";
	    try{
	    	FileWriter myWriter = new FileWriter(newFile.getAbsoluteFile(),true);
	    	java.util.Properties config = new java.util.Properties(); 
	    	config.put("StrictHostKeyChecking", "no");
	    	JSch jsch = new JSch();
	    	Session session=jsch.getSession(user, host, 22);
	    	session.setPassword(password);
	    	session.setConfig(config);
	    	session.connect();
	    	System.out.println("Connected");
	    	
	    	Channel channel=session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command1);
	        channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        
	        InputStream in=channel.getInputStream();
	        channel.connect();
	        byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
	            //System.out.print(new String(tmp, 0, i));
	            myWriter.write(new String(tmp, 0, i));
	          }
	          if(channel.isClosed()){
	            System.out.println("exit-status: "+channel.getExitStatus());
	            myWriter.close();
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	        }
	        channel.disconnect();
	        session.disconnect();
	        System.out.println("DONE");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }

	}

}
