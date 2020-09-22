package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import framework.JolokiaCall;
import operations.WriteToFile;
import utility.ExportToText;

import javax.swing.JTabbedPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.awt.event.ActionEvent;

public class MemoryWindow extends JFrame {

	private JPanel contentPane;
	Map<String,Thread> mapThread = new HashMap<String,Thread>();
	JTextArea textArea = new JTextArea();
	Properties config;
	public static String basePath = getUsersHomeDir() + File.separator + "ControllerTasks_Memory" ;
	String Controller_username = new String();
	String Controller_password=new String();
	
	String Eric_Ip = new String();
	String Eric_username = new String();
	String Eric_password=new String();
	
	String SIAE_Ip = new String();
	String SIAE_username = new String();
	String SIAE_password=new String();
	
	String Huawei_Ip = new String();
	String Huawei_username = new String();
	String Huawei_password=new String();
	
	Map<String,String> EricssonWindows = new HashMap<String,String>();
	
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MemoryWindow frame = new MemoryWindow();
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
	public MemoryWindow() {
		loadProperties();
		if(!new File(basePath).exists())
	    {
	    	new File(basePath).mkdir();
	    	System.out.println("Directory is created!");
	    }
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 771, 465);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Controller", null, panel, null);
		
		JButton btnStart = new JButton("Start");		
		JButton btnStop = new JButton("Stop");		
		JButton btnSave = new JButton("Save");
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnClear = new JButton("Clear");
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnStart)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnStop)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 467, Short.MAX_VALUE)
							.addComponent(btnSave))
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE))
					.addGap(8))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(20)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnStart)
						.addComponent(btnStop)
						.addComponent(btnSave)
						.addComponent(btnClear))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
					.addGap(8))
		);
		
		scrollPane.setViewportView(textArea);
		panel.setLayout(gl_panel);
		
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Controller
				Thread thread = mapThread.get("Controller");
				thread.stop();
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Controller
				new ExportToText(textArea.getText()).exportToText();
			}
		});
		
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
        		textArea.setCaretPosition(textArea.getDocument().getLength());
			}
		});
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//jolokia
				Thread thread = new Thread(new Runnable() {
			        @Override
			        public void run() {
			        	try
			        	{
			        	SimpleDateFormat formatter1 = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
				        WriteToFile writer = new WriteToFile(MemoryWindow.basePath + File.separator + "Controller"
				        + "_"  + formatter1.format(new Date()));
			        	JolokiaCall call = new JolokiaCall(); 
			        	String header = "timeStamp			max			used\n";
						textArea.append(header);	
						writer.write(header);
			        	for(int i=4;i>1;i++)
			        	{
			        		Map<String,String> res = call.getJolokiaResponse();  
			        		String output = res.get("timeStamp")+"		"+
			        				res.get("max")+"		"+ res.get("used")+"\n";
			        		textArea.append(output);
			        		textArea.setCaretPosition(textArea.getDocument().getLength());
			        		writer.write(output);
			        		try {
							    Thread.sleep(10000);}catch(Exception ex) {}
			        	}
			        	writer.close();
			        }catch(Exception ex)
			        	{
			        	
			        	}
			        }});
				thread.start();
				mapThread.put("Controller", thread);
			}
		});
				
		Iterator<Map.Entry<String, String>> EricWindows = EricssonWindows.entrySet().iterator();
		while(EricWindows.hasNext())
		{
	    Map.Entry<String, String> mapEntry = EricWindows.next(); 
		String instance = mapEntry.getKey();
	    String command = mapEntry.getValue();
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Ericsson"+"_"+instance, null, panel_1, null);
		JButton button = new JButton("Start");		
		JButton button_1 = new JButton("Stop");		
		JButton button_2 = new JButton("Save");		
		JButton button_9 = new JButton("Clear");
		JTextArea textArea_1 = new JTextArea();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(button)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(button_1)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(button_9, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 467, Short.MAX_VALUE)
							.addComponent(button_2))
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE))
					.addGap(8))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(20)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(button)
						.addComponent(button_1)
						.addComponent(button_2)
						.addComponent(button_9))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
					.addGap(8))
		);
		
		scrollPane_1.setViewportView(textArea_1);
		panel_1.setLayout(gl_panel_1);
		
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Controller
				new ExportToText(textArea_1.getText()).exportToText();
			}
		});
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Ericsson
				Thread thread = mapThread.get("Ericsson");
				thread.stop();
			}
		});
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea_1.setText("");
				textArea_1.setCaretPosition(textArea_1.getDocument().getLength());
			}
		});
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//pocmederi
				Thread thread = new Thread(new Runnable() {
			        @Override
			        public void run() {
			        for(;;) {
			        
					String host=Eric_Ip;
				    String user=Eric_username;
				    String password=Eric_password;
				    String command1=command;
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
				        SimpleDateFormat formatter1 = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
				        WriteToFile writer = new WriteToFile(MemoryWindow.basePath + File.separator + "Ericsson"
				        + "_" +instance+"_" + formatter1.format(new Date()));
				        byte[] tmp=new byte[1024];
				        textArea_1.append("TIMESTAMP\t\tMEMORY UTILIZATION\n");
				        textArea_1.append("-------------------------------------------------------------------------------\n");
				        while(true){
				          while(in.available()>0){
				            int i=in.read(tmp, 0, 1024);
				            if(i<0)break;
				            String[] output = new String(tmp,0,i).split("\n");
				            textArea_1.append(formatter.format(new Date())+ "  " + output[0] + "\n" );
				            textArea_1.setCaretPosition(textArea.getDocument().getLength());
				            writer.write(output[0]+"\n");
				          }
				          if(channel.isClosed()){
				            System.out.println("exit-status: "+channel.getExitStatus());
				            break;
				          }
				          try{Thread.sleep(10000);}catch(Exception ee){}
				        }
				        channel.disconnect();
				        session.disconnect();
				        System.out.println("DONE");
				        writer.close();
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
				    try {
				    Thread.sleep(1000);}catch(Exception ex) {}
			        }}});
					mapThread.put("Ericsson", thread);
					thread.start();
					
			}
		});
		}
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Huawei", null, panel_2, null);
		
		JButton button_3 = new JButton("Start");		
		JButton button_4 = new JButton("Stop");		
		JButton button_5 = new JButton("Save");
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
		JButton button_10 = new JButton("Clear");
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(button_3)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(button_4)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(button_10, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 468, Short.MAX_VALUE)
							.addComponent(button_5))
						.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE))
					.addGap(8))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(20)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(button_3)
						.addComponent(button_4)
						.addComponent(button_5)
						.addComponent(button_10))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
					.addGap(8))
		);
		
		JTextArea textArea_2 = new JTextArea();
		scrollPane_2.setViewportView(textArea_2);
		panel_2.setLayout(gl_panel_2);
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Controller
				new ExportToText(textArea_2.getText()).exportToText();
			}
		});
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//huawei
				Thread thread = mapThread.get("Huawei");
				thread.stop();
			}
		});
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea_2.setText("");
				textArea_2.setCaretPosition(textArea_2.getDocument().getLength());
			}
		});
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Huawei
				Thread thread = new Thread(new Runnable() {
			        @Override
			        public void run() {
					String host=Huawei_Ip;
				    String user=Huawei_username;
				    String password=Huawei_password;
				    String command1="top | grep java";
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
				        ((ChannelExec)channel).setPty(true);
				        InputStream in=channel.getInputStream();
				        channel.connect();
				        SimpleDateFormat formatter1 = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
				        WriteToFile writer = new WriteToFile(MemoryWindow.basePath + File.separator + "Huawei"
				        + "_"  + formatter1.format(new Date()));
				        byte[] tmp=new byte[1024];
				        textArea_2.append("TIMESTAMP\t              PID USER      PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+ COMMAND\r\n" + 
				        		"");
				        textArea_2.append("------------------------------------------------------------------------------------------------"
				        		+ "-----------------------------------------------------------------------------------"
				        		+ "-----------------------------------------------\n");
				        while(true){
				          while(in.available()>0){
				            int i=in.read(tmp, 0, 1024);
				            if(i<0)break;
				            String output = formatter.format(new Date())+ "  " +
						            new String(tmp, 0, i).replace("[m", "").replace("[m[K", "").replace(" [K\r\n" + 
						            		"","") + "\n";
				            textArea_2.append(output);
				            textArea_2.setCaretPosition(textArea.getDocument().getLength());
				            writer.write(output);
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
				        writer.close();
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			            }});
					mapThread.put("Huawei", thread);
					thread.start();
			}
		});
		
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("SIAE", null, panel_3, null);
		
		JButton button_6 = new JButton("Start");		
		JButton button_7 = new JButton("Stop");		
		JButton button_8 = new JButton("Save");
		
		JScrollPane scrollPane_3 = new JScrollPane();
		
		JButton button_11 = new JButton("Clear");
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(button_6)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(button_7)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(button_11, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 466, Short.MAX_VALUE)
							.addComponent(button_8))
						.addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE))
					.addGap(8))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(20)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
						.addComponent(button_6)
						.addComponent(button_7)
						.addComponent(button_8)
						.addComponent(button_11))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
					.addGap(8))
		);
		
		JTextArea textArea_3 = new JTextArea();
		scrollPane_3.setViewportView(textArea_3);
		panel_3.setLayout(gl_panel_3);
		button_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea_3.setText("");
				textArea_3.setCaretPosition(textArea_3.getDocument().getLength());
			}
		});
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Controller
				new ExportToText(textArea_3.getText()).exportToText();
			}
		});
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//SIAE
				Thread thread = mapThread.get("SIAE");
				thread.stop();
			}
		});
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//SIAE
				Thread thread = new Thread(new Runnable() {
			        @Override
			        public void run() {
					String host=SIAE_Ip;
				    String user=SIAE_username;
				    String password=SIAE_password;
				    String command1=SIAE_password +" | sudo docker container stats";
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
				        ((ChannelExec)channel).setPty(true);
				        
				        InputStream in=channel.getInputStream();
				        OutputStream out=channel.getOutputStream();
				        channel.connect();
				        out.write((password+"\n").getBytes());
				        out.flush();
				        SimpleDateFormat formatter1 = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
				        WriteToFile writer = new WriteToFile(MemoryWindow.basePath + File.separator + "SIAE"
				        + "_"  + formatter1.format(new Date()));
				        byte[] tmp=new byte[1024];
				        String header = "TIMESTAMP        	        	CONTAINER ID        NAME                       CPU %           "
				        		+ "  MEM USAGE / LIMIT     MEM %               NET I/O                  BLOCK I/O     "
				        		+ "        PIDS\r\n";
				        String border = "------------------------------------------------------------------------------------------------"
				        		+ "-----------------------------------------------------------------------------------"
				        		+ "-----------------------------------------------\n";
				        textArea_3.append(header);
				        textArea_3.append(border);
				        writer.write(header);
				        writer.write(border);
				        while(true){
				          while(in.available()>0){
				            int i=in.read(tmp, 0, 1024);
				            if(i<0)break;
				            String[] output = new String(tmp, 0, i).split("\n");
				            for(int k=0;k<output.length;k++)
				            {
					            if(!output[k].contains("command not found") && !output[k].contains("[sudo] password")
					            		&& !output[k].contains("CONTAINER ID") && !output[k].contains("CPU %")
					            		&& !output[k].contains("PIDS") && !output[k].contains("MEM USAGE / LIMIT   MEM %") 
					            		&& !output[k].contains("NET I/O             BLOCK I/O")
					            		&& !output[k].isEmpty() && !output[k].contains("[2J[H")
					            		&& !output[k].equals("\r"))
					            {
					            String finaloutput = output[k].replace("[2J[H", "");
					            String finalout = formatter.format(new Date())+ "\t" +finaloutput + "\n";
					            textArea_3.append(finalout);
					            textArea_3.setCaretPosition(textArea.getDocument().getLength());
					            writer.write(finalout);
					            }
				            }
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
				        writer.close();
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			            }});
					mapThread.put("SIAE", thread);
					thread.start();
			}
		});
	}
	
	public void loadProperties()
	{
		config = new Properties();
		try {
			 FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/Config.properties");
			 config.load(fis);
			
			 Controller_username = config.getProperty("Controller.username");
			 Controller_password= config.getProperty("Controller.password");
			 
			 Eric_Ip = config.getProperty("Mediator.ericsson.ip");
			 Eric_username = config.getProperty("Mediator.ericsson.username");
			 Eric_password=config.getProperty("Mediator.ericsson.password");
			 
			 SIAE_Ip =config.getProperty("Mediator.SIAE.ip");
			 SIAE_username = config.getProperty("Mediator.SIAE.username");
			 SIAE_password=config.getProperty("Mediator.SIAE.password");
			
			 Huawei_Ip =config.getProperty("Mediator.Huawei.ip");
			 Huawei_username =config.getProperty("Mediator.Huawei.username");
			 Huawei_password=config.getProperty("Mediator.Huawei.password");
			 
			 int instanceCount = Integer.parseInt(config.getProperty("Memory.Ericsson.instance.count"));
			 for(int i=1;i<=instanceCount;i++)
			 {
				 String instance = config.getProperty("Memory.Ericsson.instance."+i);
				 String command  = config.getProperty("Memory.Ericsson.command."+i);
				 EricssonWindows.put(instance, command);
			 }
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getUsersHomeDir() {
	    String users_home = System.getProperty("user.home");
	    return users_home.replace("\\", "/"); // to support all platforms.
	}
}
