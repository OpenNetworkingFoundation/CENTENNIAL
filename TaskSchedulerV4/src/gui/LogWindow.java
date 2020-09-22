package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import framework.ControllerConfig;
import operations.WriteToFile;
import utility.ExportToText;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;

public class LogWindow extends JFrame {

	private JPanel contentPane;
	
	JTabbedPane erricssonPane =new JTabbedPane(JTabbedPane.TOP);
	JTabbedPane huwaeiPane = new JTabbedPane(JTabbedPane.TOP);
	JTabbedPane siaePane = new JTabbedPane(JTabbedPane.TOP);
	JTextArea textArea = new JTextArea();	
	Map<String,Thread> ThreadMap = new HashMap<String,Thread>();
	Map<String,JTextArea> TextMap = new HashMap<String,JTextArea>();
	Properties config;
	public static String basePath = getUsersHomeDir() + File.separator + "ControllerTasks_Logs" ;
	
	String Controller_username = new String();
	String Controller_password=new String();
	String Controller_logPath=new String();
	String Controller_logFile=new String();
	
	String Eric_Ip = new String();
	String Eric_username = new String();
	String Eric_password=new String();
	String Eric_logPath=new String();
	String Eric_logFile=new String();
	ArrayList EricssonWindows = new ArrayList();
	
	String SIAE_Ip = new String();
	String SIAE_username = new String();
	String SIAE_password=new String();
	String SIAE_logPath=new String();
	ArrayList SIAEWindows = new ArrayList();
	
	String Huawei_Ip = new String();
	String Huawei_username = new String();
	String Huawei_password=new String();
	String Huawei_logPath=new String();
	String Huawei_logFile=new String();
	
	class Vendor
	{
		public String host= new String();
		public String user=new String();
		public String password=new String();
		public String command=new String();
		
		public Vendor(String host,String user,String password,String command)
		{
			this.host = host;
			this.user = user;
			this.password = password;
			this.command = command;
		}
		
	}
	
	public static Map<String,Vendor> logDetails = new HashMap<String,Vendor>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogWindow frame = new LogWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LogWindow() {
		loadProperties();
		if(!new File(basePath).exists())
	    {
	    	new File(basePath).mkdir();
	    	System.out.println("Directory is created!");
	    }
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 769, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Controller Log", null, panel, null);
		
		JButton btnNewButton = new JButton("Start");		
		JButton btnNewButton_1 = new JButton("Stop");		
		JButton btnNewButton_2 = new JButton("Save");
		btnNewButton_1.setActionCommand("Controller");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String actionString = arg0.getActionCommand();
				Thread th = ThreadMap.get(actionString);
				th.stop();
			}
		});
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ExportToText(textArea.getText()).exportToText();
			}
		});		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnNewButton)
							.addGap(18)
							.addComponent(btnNewButton_1)
							.addPreferredGap(ComponentPlacement.RELATED, 516, Short.MAX_VALUE)
							.addComponent(btnNewButton_2)
							.addGap(28))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(0)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE)
							.addGap(12))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton)
						.addComponent(btnNewButton_1)
						.addComponent(btnNewButton_2))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
					.addGap(7))
		);
		
		textArea.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setViewportView(textArea);
		panel.setLayout(gl_panel);
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logDetails();				
			}
		});
		
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Mediator Logs", null, tabbedPane_1, null);
		tabbedPane_1.addTab("Ericsson", null, erricssonPane, null);				
		tabbedPane_1.addTab("Huawei", null, huwaeiPane, null);	
		tabbedPane_1.addTab("SIAE", null, siaePane, null);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
		);
		contentPane.setLayout(gl_contentPane);
		getFolderNamesforEricsson();
		getFolderNamesforHuawei();
		getFolderNamesforSIAE();
	}
	
	public void getFolderNamesforEricsson()
	{
		//ArrayList<String> list = getConsoleDetails(Eric_Ip,Eric_username,Eric_password,"cd "+Eric_logPath+";ls");
		Iterator<String> iter = EricssonWindows.iterator();
		while(iter.hasNext())
		{
			String foldername = iter.next();
			String[] folders = foldername.split("\n");
			for(int i=0;i<folders.length;i++)
			{
				if(folders[i].startsWith("CO-"))
				{
					addConsoleWindow(folders[i], erricssonPane, "Ericsson");
				}
			}			
		}
	}
	
	public void loadProperties()
	{
		config = new Properties();
		try {
			FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/Config.properties");
			config.load(fis);
			
			 Controller_username = config.getProperty("Controller.username");
			 Controller_password= config.getProperty("Controller.password");
			 Controller_logPath=config.getProperty("Controller.logPath");
			 Controller_logFile=config.getProperty("Controller.logFile");
			 
			 Eric_Ip = config.getProperty("Mediator.ericsson.ip");
			 Eric_username = config.getProperty("Mediator.ericsson.username");
			 Eric_password=config.getProperty("Mediator.ericsson.password");
			 Eric_logPath=config.getProperty("Mediator.ericsson.logPath");
			 Eric_logFile=config.getProperty("Mediator.ericsson.logFile");
			 
			 SIAE_Ip =config.getProperty("Mediator.SIAE.ip");
			 SIAE_username = config.getProperty("Mediator.SIAE.username");
			 SIAE_password=config.getProperty("Mediator.SIAE.password");
			 SIAE_logPath=config.getProperty("Mediator.SIAE.logPath");
			
			 Huawei_Ip =config.getProperty("Mediator.Huawei.ip");
			 Huawei_username =config.getProperty("Mediator.Huawei.username");
			 Huawei_password=config.getProperty("Mediator.Huawei.password");
			 Huawei_logPath=config.getProperty("Mediator.Huawei.logPath");
			 Huawei_logFile=config.getProperty("Mediator.Huawei.logFile");
			 
			 String[] ericWindow = config.getProperty("Mediator.ericsson.instances").split(",");
			 String[] siaWindow = config.getProperty("Mediator.SIAE.instances").split(",");
			 for(int i=0;i<ericWindow.length;i++)
			 {
				 EricssonWindows.add(ericWindow[i]);
			 }
			 for(int i=0;i<siaWindow.length;i++)
			 {
				 SIAEWindows.add(siaWindow[i]);
			 }
			 
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void getFolderNamesforHuawei()
	{
		addConsoleWindow("mediatorlog", huwaeiPane, "Huawei");		
	}
	
	public void getFolderNamesforSIAE()
	{
		//ArrayList<String> list = getConsoleDetails(SIAE_Ip,SIAE_username,SIAE_password," cd "+SIAE_logPath+";ls");
		Iterator<String> iter = SIAEWindows.iterator();
		while(iter.hasNext())
		{
			String foldername = iter.next();
			String[] folders = foldername.split("\n");
			for(int i=0;i<folders.length;i++)
			{
				addConsoleWindow(folders[i], siaePane, "SIAE");
			}			
		}
	}
	
	public void addConsoleWindow(String nodeId, JTabbedPane tab, String vendor )
	{	    
	    if(vendor.equals("Ericsson"))
	    {
	    	logDetails.put(vendor+":"+nodeId, new Vendor(Eric_Ip,Eric_username,Eric_password,"cd "+Eric_logPath
	    			+nodeId+ ";tail -F "+Eric_logFile));
	    }else if(vendor.equals("Huawei"))
	    {
	    	logDetails.put(vendor+":"+nodeId, new Vendor(Huawei_Ip,Huawei_username,Huawei_password,"cd "+ Huawei_logPath +";tail -F "+Huawei_logFile));
	    }else
	    {
	    	logDetails.put(vendor+":"+nodeId, new Vendor(SIAE_Ip,SIAE_username,SIAE_password,"cd "+SIAE_logPath
	    			+nodeId+";tail -F netconf-" + nodeId +".log"));
	    }
	    
		JPanel MediatorPanel = new JPanel();
		tab.addTab(nodeId, null, MediatorPanel, null);
		JButton btnStart = new JButton("Start");		
		JButton btnStop = new JButton("Stop");		
		JButton btnSave = new JButton("Save");
		btnStop.setActionCommand(vendor+":"+nodeId);
		btnStart.setActionCommand(nodeId);
		btnSave.setActionCommand(vendor+":"+nodeId);
		JScrollPane MediatorPane_1 = new JScrollPane();
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		MediatorPane_1.setViewportView(textArea_1);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Vendor ven = logDetails.get(vendor+":"+nodeId);				
				MediatorLogDetails(nodeId,vendor,ven.host,ven.user,ven.password,ven.command,textArea_1,vendor+":"+nodeId);				
			}
		});
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String actionString = arg0.getActionCommand();
				Thread th = ThreadMap.get(actionString);
				th.stop();
			}
		});
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new ExportToText(TextMap.get(arg0.getActionCommand()).getText()).exportToText();
			}
		});
		TextMap.put(vendor+":"+nodeId, textArea_1);
		GroupLayout gl_MediatorPanel = new GroupLayout(MediatorPanel);
		gl_MediatorPanel.setHorizontalGroup(
			gl_MediatorPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_MediatorPanel.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_MediatorPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_MediatorPanel.createSequentialGroup()
							.addComponent(btnStart)
							.addGap(18)
							.addComponent(btnStop)
							.addGap(515)
							.addComponent(btnSave)
							.addGap(4))
						.addComponent(MediatorPane_1, GroupLayout.DEFAULT_SIZE, 709, Short.MAX_VALUE))
					.addGap(12))
		);
		gl_MediatorPanel.setVerticalGroup(
			gl_MediatorPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_MediatorPanel.createSequentialGroup()
					.addGap(11)
					.addGroup(gl_MediatorPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnStart)
						.addComponent(btnStop)
						.addComponent(btnSave))
					.addGap(6)
					.addComponent(MediatorPane_1, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
					.addGap(5))
		);
		MediatorPanel.setLayout(gl_MediatorPanel);
	}
	
	
	
	public ArrayList<String> getConsoleDetails(String host,String user,String password,String command)
	{
		ArrayList array = new ArrayList();
	    try{
	    	java.util.Properties config = new java.util.Properties(); 
	    	config.put("StrictHostKeyChecking", "no");
	    	JSch jsch = new JSch();
	    	Session session=jsch.getSession(user, host, 22);
	    	session.setPassword(password);
	    	session.setConfig(config);
	    	session.connect();
	    	System.out.println("Connected");
	    	
	    	Channel channel=session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command);
	        channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        
	        InputStream in=channel.getInputStream();
	        channel.connect();
	        byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
	            array.add(new String(tmp, 0, i));
	            //System.out.print(new String(tmp, 0, i));
	          }
	          if(channel.isClosed()){
	            System.out.println("exit-status: "+channel.getExitStatus());
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
	    return array;
	}
	
	public void logDetails()
	{
		
		Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
		String host=ControllerConfig.IP;
	    String user=Controller_username;
	    String password=Controller_password;
	    String command1="cd "+Controller_logPath+";tail -F "+Controller_logFile;
	    try{
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
	        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
	        WriteToFile writer = new WriteToFile(LogWindow.basePath + File.separator + "Controller"
	        + "_" + formatter.format(new Date()));
	        byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
	            textArea.append(new String(tmp, 0, i));
	            textArea.setCaretPosition(textArea.getDocument().getLength());
	            writer.write(new String(tmp, 0, i));
	            //System.out.print(new String(tmp, 0, i));
	          }
	          if(channel.isClosed()){
	            System.out.println("exit-status: "+channel.getExitStatus());
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	        }
	        channel.disconnect();
	        session.disconnect();
	        writer.close();
	        System.out.println("DONE");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
            }});
		thread.start();
	    ThreadMap.put("Controller", thread);
	}
	
	public void MediatorLogDetails(String nodeid,String vendor,String host,String user,String password,String command,JTextArea textArea1,String identity)
	{
		System.out.println(host + "+ " + user + "+" + password + "+" +command );
		Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
	    try{
	    	java.util.Properties config = new java.util.Properties(); 
	    	config.put("StrictHostKeyChecking", "no");
	    	JSch jsch = new JSch();
	    	Session session=jsch.getSession(user, host, 22);
	    	session.setPassword(password);
	    	session.setConfig(config);
	    	session.connect();
	    	System.out.println("Connected");
	    	
	    	Channel channel=session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command);
	        channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        
	        InputStream in=channel.getInputStream();
	        channel.connect();
	        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
	        WriteToFile writer = new WriteToFile(LogWindow.basePath + File.separator + vendor + "_" 
	        		+nodeid + "_" + formatter.format(new Date()));
	        byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
	            textArea1.append(new String(tmp, 0, i));
	            textArea1.setCaretPosition(textArea1.getDocument().getLength());
	            writer.write(new String(tmp, 0, i));
	            //System.out.print(new String(tmp, 0, i));
	          }
	          if(channel.isClosed()){
	            System.out.println("exit-status: "+channel.getExitStatus());
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	        }
	        channel.disconnect();
	        session.disconnect();
	        writer.close();
	        System.out.println("DONE");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
            }});
		thread.start();
		ThreadMap.put(identity, thread);
	}
	
	public static String getUsersHomeDir() {
	    String users_home = System.getProperty("user.home");
	    return users_home.replace("\\", "/"); // to support all platforms.
	}
}
