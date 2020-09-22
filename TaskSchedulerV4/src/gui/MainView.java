package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.awt.Color;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.json.JSONObject;
import org.quartz.JobDetail;

import com.sun.jna.platform.FileUtils;

import framework.ParallelRestCall;
import framework.SimpleParallelRestCallThread;
import framework.SimpleRestCallThread;
import operations.LoginToController;
import operations.WriteToFile;
import resources.CreateTestSuite;
import resources.CurrentTestSuite;
import resources.Repository;
import resources.TaskDetails;
import scheduler.CustomScheduler;
import scheduler.CustomSchedulerParallel;
import utility.ExportFolderToExcel;
import utility.ExportTaskFile;
import utility.ExportToExcel;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.TextField;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Component;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class MainView extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField taskName;
	public static JTextField testSuite;
	public static JTree tree;
	JComboBox comboBox = new JComboBox();
	
	/**
	 * Launch the application.
	 */
	/**public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}**/

	/**
	 * Create the frame.
	 */
	public MainView() {
		
		String path = getUsersHomeDir() + File.separator + "ControllerTasks" ;
		if(!new File(path).exists())
	    {
	    	new File(path).mkdir();
	    	System.out.println("Directory is created!");
	    }
				
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 788, 495);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExportToExcel = new JMenuItem("Export to Excel");
		mntmExportToExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//export the current table to excel sheet
				try
				{
				Progress wait = new Progress("Export to excelsheet");
    			SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {	
    				@Override
				    protected Void doInBackground() throws Exception {
    					ExportToExcel.exportToExcel(table.getModel());
    					wait.close();
				        return null;
    				 }
				};
				mySwingWorker.execute();
				wait.makeMenuWait("Test", arg0);				   		
				}catch(Exception e)
				{
					JOptionPane.showMessageDialog(null, "unable to export");
			    	return;
				}				
			}
		});
		mnFile.add(mntmExportToExcel);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JMenuItem mntmLogWindow = new JMenuItem("Log Window");
		mntmLogWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Open Log window
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
		});
		mnView.add(mntmLogWindow);
		
		JMenuItem mntmMemoryWindow = new JMenuItem("Memory Window");
		mntmMemoryWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Open Memory window
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
		});
		mnView.add(mntmMemoryWindow);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		File file = new File(path);
		ArrayList TaskDirectories = new ArrayList(Arrays.asList(file.list()));
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("ControllerTasks");
		Iterator<String> TaskDirectoriesiter = TaskDirectories.iterator();
		while(TaskDirectoriesiter.hasNext())
		{
			String directoryName = TaskDirectoriesiter.next();
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(directoryName);
			File taskFile = new File(path + File.separator + directoryName );
			ArrayList taskFileDirectories = new ArrayList(Arrays.asList(taskFile.list()));
			Iterator<String> taskFileDirectoriesiter = taskFileDirectories.iterator();
			while(taskFileDirectoriesiter.hasNext())
			{
				DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(taskFileDirectoriesiter.next());
				node.add(fileNode);
			}
			root.add(node);
		}
		
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE))
					.addGap(0))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
		);
		
		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("Results", null, scrollPane, null);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"No", "Details", "TestSuite", "context", "URL Details", "Start Time", "End Time", "Status", "Response Time", "Response"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(15);
		scrollPane.setViewportView(table);
		table.getColumn("Response").setCellRenderer(new ButtonRenderer());
		table.getColumn("Response").setCellEditor(new ButtonEditor(new JCheckBox()));
		tree = new JTree(root);
		tree.getSelectionModel().addTreeSelectionListener(new Selector());
	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    
		scrollPane_1.setViewportView(tree);
		tree.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(tree, popupMenu);
		
		JMenuItem mntmExportToExcel_1 = new JMenuItem("Export To Excel");
		mntmExportToExcel_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//prathiba
				//get the selected folder name
				String lastSelectedPath = tree.getLastSelectedPathComponent().toString();
				String rootPath = getUsersHomeDir() + File.separator + "ControllerTasks" ;
	  	      	String folderName = tree.getLastSelectedPathComponent().toString();
	  	      	String completePath = rootPath + File.separator + folderName;
	  	      	System.out.println(completePath);
	  	    //export the current table to excel sheet
				try
				{
				Progress wait = new Progress("Export to excelsheet");
    			SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {	
    				@Override
				    protected Void doInBackground() throws Exception {
    					ExportFolderToExcel.exportToExcel(completePath);
    					wait.close();
				        return null;
    				 }
				};
				mySwingWorker.execute();
				wait.makeMenuWait("Test", e);				   		
				}catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, "unable to export");
			    	return;
				}		
	  	        
			}
		});
		
		JMenuItem mntmStopTask = new JMenuItem("Stop Task");
		mntmStopTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
				String folderName = tree.getLastSelectedPathComponent().toString();
				JobDetail newSchedule = CustomScheduler.SchedulerRepo.get(folderName);
				CustomScheduler.sched.deleteJob(newSchedule.getKey());
				}catch(Exception ex)
				{}
			}
		});
		popupMenu.add(mntmStopTask);
		
		JMenuItem mntmDeleteTask = new JMenuItem("Delete Task");
		mntmDeleteTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
				String folderName = tree.getLastSelectedPathComponent().toString();
				JobDetail newSchedule = CustomScheduler.SchedulerRepo.get(folderName);
				if(newSchedule!=null)
				{
				CustomScheduler.sched.deleteJob(newSchedule.getKey());
				}
				}catch(Exception ex)
				{}
				try
				{
				String folderName = tree.getLastSelectedPathComponent().toString();
				String path = getUsersHomeDir() + File.separator + "ControllerTasks" ;
				File file = new File(path + File.separator +folderName);
				if(file.exists() && file.isDirectory())
				{				
					Boolean bol = MainView.recursiveDelete(file);					
					if(bol)
					{
					DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
				    	DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();				    	
					    Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
					    while (e.hasMoreElements()) {
					        DefaultMutableTreeNode node = e.nextElement();
					        if (node.toString().equalsIgnoreCase(folderName)) {
					        	root.remove(node);
					        }
					    }
					model.reload(root);
					((DefaultTreeModel)MainView.tree.getModel()).reload();
					JOptionPane.showMessageDialog(null, "stopped and deleted the task");
				
					}else
					{
					JOptionPane.showMessageDialog(null, "Unable to delete it , please manually delete it from "+file.getAbsolutePath());	
					}
				}
				}catch(Exception ex)
				{
					
				}
			}
		});
		popupMenu.add(mntmDeleteTask);
		
		JMenuItem mntmSaveTask = new JMenuItem("Save Task");
		mntmSaveTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//prathiba
				//get the selected folder name
				String lastSelectedPath = tree.getLastSelectedPathComponent().toString();
				String rootPath = getUsersHomeDir() + File.separator + "ControllerTasks" ;
	  	      	String folderName = tree.getLastSelectedPathComponent().toString();
	  	      	String completePath = rootPath + File.separator + folderName + File.separator + "Task.info";
	  	      	System.out.println(completePath);
	  	    //export the current table to excel sheet
				try
				{
				Progress wait = new Progress("generating task file");
    			SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {	
    				@Override
				    protected Void doInBackground() throws Exception {
    					ExportTaskFile expo = new ExportTaskFile(new File(completePath));
    					expo.exportToText();
    					wait.close();
				        return null;
    				 }
				};
				mySwingWorker.execute();
				wait.makeMenuWait("Test", e);				   		
				}catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, "unable to export");
			    	return;
				}		
	  	        
			}
		});
		popupMenu.add(mntmSaveTask);
		popupMenu.add(mntmExportToExcel_1);
		
		JLabel lblNewLabel = new JLabel("Create New Task");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblTaskName = new JLabel("Task Name");
		lblTaskName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblFrequency = new JLabel("Frequency");
		lblFrequency.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblTestSuite = new JLabel("Test Suite");
		lblTestSuite.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		taskName = new JTextField();
		taskName.setBorder(new LineBorder(new Color(171, 173, 179)));
		taskName.setColumns(10);
		
		testSuite = new JTextField();
		testSuite.setBorder(new LineBorder(new Color(171, 173, 179)));
		testSuite.setEnabled(false);
		testSuite.setColumns(10);
	
		ButtonGroup radioGroup = new ButtonGroup();
		JButton btnNewButton = new JButton("Configure");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginToController controller = new LoginToController();
				JSONObject jsonResponse = controller.getNodeDetails();
				controller.processResponse(jsonResponse);			
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							SchedulerPanel window = new SchedulerPanel();
							SchedulerPanel.reference = window;
							window.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		JSpinner Hours = new JSpinner();
		Hours.setModel(new SpinnerNumberModel(0, 0, 24, 1));
		
		JSpinner Minutes = new JSpinner();
		Minutes.setModel(new SpinnerNumberModel(0, 0, 60, 1));
		
		JSpinner Seconds = new JSpinner();
		Seconds.setModel(new SpinnerNumberModel(0, 0, 60, 1));
		
		JButton btnCreate = new JButton("Create");
		JRadioButton rdbtnSerial = new JRadioButton("Serial");		
		JRadioButton rdbtnParallel = new JRadioButton("Parallel");
		radioGroup.add(rdbtnSerial);
		radioGroup.add(rdbtnParallel);
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//get all the userdefined values and create a object
				
				int dur = comboBox.getSelectedIndex();
				String duration = "none";
				if(dur != 0)
				{
					duration = Integer.toString(dur);
				}
				String taskNameDetails = taskName.getText();
				if(taskNameDetails.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Please provide a Task Name and continue");
			    	return;
				}else if(!taskNameDetails.matches("[A-Za-z0-9]+$")) //"^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]+$"
				{
					JOptionPane.showMessageDialog(null, "Please dont include any special charecters in the Task name. \n modify the task name and try again");
			    	return;
				}
				String descriptionString = "dummy";
				if(!rdbtnSerial.isSelected() && !rdbtnParallel.isSelected() )
				{
					JOptionPane.showMessageDialog(null, "Please Select a Execution type");
			    	return;
				}
				String executionType = "Serial";
				if(rdbtnParallel.isSelected())
				{
					executionType = "parallel";
				}
				String selectedFrequency = Hours.getValue().toString() + "-" + Minutes.getValue().toString() + "-" + Seconds.getValue().toString();
				
				if(testSuite.getText().isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Please Select atlease one testsuit");
			    	return;
				}
				//Create a folder in the name of the task
				String subPath = path + File.separator + taskNameDetails;
				if(!new File(subPath).exists())
			    {
			    	new File(subPath).mkdir();
			    	String taskfullDetails = new String();
			    	String suiteDetails = new String();
			    	String protocol = new String();
			    	String lpDetail = new String();
			    	System.out.println("Directory is created!");
			    	DefaultTreeModel model1 = (DefaultTreeModel)tree.getModel();
			    	DefaultMutableTreeNode root1 = (DefaultMutableTreeNode)model1.getRoot();
			    	root1.add(new DefaultMutableTreeNode(taskNameDetails));
			    	model1.reload(root1);
			    	//load the testsuites
			    	ArrayList<CurrentTestSuite> suiteList = new ArrayList();
			    	Iterator<CurrentTestSuite> suiteIterator = CurrentTestSuite.CurrentList.iterator();
			    	while(suiteIterator.hasNext())
			    	{
			    		suiteList.add(suiteIterator.next());
			    	}
			    	//Schedule the task
			    	//create the task and maintain it in the repository
			    	if(!selectedFrequency.equals("0-0-0"))
			    	{
			    		if(executionType.equalsIgnoreCase("Serial"))
						{
							TaskDetails newTask = new TaskDetails(taskNameDetails,descriptionString,suiteList,"Serial",selectedFrequency,duration);
							TaskDetails.taskRepo.put(newTask.taskName,newTask);
							//schedule a task for the granularity
							CustomScheduler newSchedule = new CustomScheduler(taskNameDetails,"Serial",duration);
							try
							{
							newSchedule.run();
							}catch(Exception ex) 
							{
								JOptionPane.showMessageDialog(null, "something went wrong.. Please restart the application");
							}
							Iterator<CurrentTestSuite> saveiter = suiteList.iterator();
							while(saveiter.hasNext())
							{
								CurrentTestSuite suite = saveiter.next();
								if(suite.Details.contains(":"))
								{
								protocol = Repository.MapLogicalProtocolMap.get(suite.Details.split(":")[0]).get(suite.Details.split(":")[1]);
								lpDetail = Repository.MapLTP_LPMap.get(suite.Details.split(":")[0]).get(suite.Details.split(":")[1]);
								}else
								{
								protocol = "none";
								lpDetail = "none";
								}
								suiteDetails = suiteDetails + suite.no + "::" + suite.type + "::" + suite.testSuite + "::"
								+ suite.Details + "::" + suite.context + "::" + protocol + "::" + lpDetail
								+ "\n";
							}
							taskfullDetails = taskNameDetails + "::" + descriptionString + "::" + "Serial" + "::" + selectedFrequency + "::" + duration + "\n";
						}else
						{
							TaskDetails newTask = new TaskDetails(taskNameDetails,descriptionString,suiteList,"Parallel",selectedFrequency,duration);
							TaskDetails.taskRepo.put(newTask.taskName,newTask);
							//schedule a task for the granularity
							CustomScheduler newSchedule = new CustomScheduler(taskNameDetails,"Parallel",duration);		
							try
							{
							newSchedule.run();
							}catch(Exception ex) 
							{
								JOptionPane.showMessageDialog(null, "something went wrong.. Please restart the application");
							}
							Iterator<CurrentTestSuite> saveiter = suiteList.iterator();
							while(saveiter.hasNext())
							{
								CurrentTestSuite suite = saveiter.next();
								if(suite.Details.contains(":"))
								{
								protocol = Repository.MapLogicalProtocolMap.get(suite.Details.split(":")[0]).get(suite.Details.split(":")[1]);
								lpDetail = Repository.MapLTP_LPMap.get(suite.Details.split(":")[0]).get(suite.Details.split(":")[1]);
								}else
								{
								protocol = "none";
								lpDetail = "none";
								}
								suiteDetails = suiteDetails + suite.no + "::" + suite.type + "::" + suite.testSuite + "::" 
								+ suite.Details + "::" + suite.context + "::" + protocol + "::" + lpDetail
								+ "\n";
							}
							taskfullDetails = taskNameDetails + "::" + descriptionString + "::" + "Parallel" + "::" + selectedFrequency + "::" + duration + "\n";
						}
					}else
					{
						if(executionType.equalsIgnoreCase("Serial"))
						{
							TaskDetails newTask = new TaskDetails(taskNameDetails,descriptionString,suiteList,"Serial",selectedFrequency,duration);
							TaskDetails.taskRepo.put(newTask.taskName,newTask);
							//execute the task in a new thread						
							new Thread(new SimpleRestCallThread(taskNameDetails)).start();
							Iterator<CurrentTestSuite> saveiter = suiteList.iterator();
							while(saveiter.hasNext())
							{
								CurrentTestSuite suite = saveiter.next();
								
								if(suite.Details.contains(":"))
								{
								protocol = Repository.MapLogicalProtocolMap.get(suite.Details.split(":")[0]).get(suite.Details.split(":")[1]);
								lpDetail = Repository.MapLTP_LPMap.get(suite.Details.split(":")[0]).get(suite.Details.split(":")[1]);
								}else
								{
								protocol = "none";
								lpDetail = "none";
								}
								suiteDetails = suiteDetails + suite.no + "::" + suite.type + "::" + suite.testSuite + "::" 
								+ suite.Details + "::" + suite.context + "::" + protocol + "::" + lpDetail
								+ "\n";
							}
							taskfullDetails = taskNameDetails + "::" + descriptionString + "::" + "Serial" + "::" + selectedFrequency + "::" + duration + "\n";
						}else
						{
							TaskDetails newTask = new TaskDetails(taskNameDetails,descriptionString,suiteList,"Parallel",selectedFrequency,duration);
							TaskDetails.taskRepo.put(newTask.taskName,newTask);
							//execute the task in a new thread						
							(new Thread(new SimpleParallelRestCallThread(taskNameDetails))).start();
							Iterator<CurrentTestSuite> saveiter = suiteList.iterator();
							while(saveiter.hasNext())
							{
								CurrentTestSuite suite = saveiter.next();
								if(suite.Details.contains(":"))
								{
								protocol = Repository.MapLogicalProtocolMap.get(suite.Details.split(":")[0]).get(suite.Details.split(":")[1]);
								lpDetail = Repository.MapLTP_LPMap.get(suite.Details.split(":")[0]).get(suite.Details.split(":")[1]);
								}else
								{
								protocol = "none";
								lpDetail = "none";
								}
								suiteDetails = suiteDetails + suite.no + "::" + suite.type + "::" + suite.testSuite + "::" 
								+ suite.Details + "::" + suite.context + "::" + protocol + "::" + lpDetail
								+ "\n";
							}
							taskfullDetails = taskNameDetails + "::" + descriptionString + "::" + "Parallel" + "::" + selectedFrequency + "::" + duration + "\n";
						}
						
					}
			    	WriteToFile write = new WriteToFile(subPath + File.separator + "Task.info");
			    	write.write(taskfullDetails);
			    	write.write(suiteDetails);
			    	write.close();
					taskName.setText("");
					testSuite.setText("");
					radioGroup.clearSelection();
					CurrentTestSuite.CurrentList.clear();
					
			    }else
			    {
			    	JOptionPane.showMessageDialog(null, "Task name already exists... Please select a different name..");
			    	return;
			    }
				
			}
		});
		
		JLabel lblExecutionType = new JLabel("Execution Type");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JLabel lblIteration = new JLabel("Iteration");
		lblIteration.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"none", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", 
				"101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121", "122", "123", "124", "125", "126", "127", "128", "129", "130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "140", "141", "142", "143", "144", "145", "146", "147", "148", "149", "150", "151", "152", "153", "154", "155", "156", "157", "158", "159", "160", "161", "162", "163", "164", "165", "166", "167", "168", "169", "170", "171", "172", "173", "174", "175", "176", "177", "178", "179", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "190", "191", "192", "193", "194", "195", "196", "197", "198", "199", "200", 
				"201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213", "214", "215", "216", "217", "218", "219", "220", "221", "222", "223", "224", "225", "226", "227", "228", "229", "230", "231", "232", "233", "234", "235", "236", "237", "238", "239", "240", "241", "242", "243", "244", "245", "246", "247", "248", "249", "250", "251", "252", "253", "254", "255", "256", "257", "258", "259", "260", "261", "262", "263", "264", "265", "266", "267", "268", "269", "270", "271", "272", "273", "274", "275", "276", "277", "278", "279", "280", "281", "282", "283", "284", "285", "286", "287", "288", "289", "290", "291", "292", "293", "294", "295", "296", "297", "298", "299", "300", 
				"301", "302", "303", "304", "305", "306", "307", "308", "309", "310", "311", "312", "313", "314", "315", "316", "317", "318", "319", "320", "321", "322", "323", "324", "325", "326", "327", "328", "329", "330", "331", "332", "333", "334", "335", "336", "337", "338", "339", "340", "341", "342", "343", "344", "345", "346", "347", "348", "349", "350", "351", "352", "353", "354", "355", "356", "357", "358", "359", "360", "361", "362", "363", "364", "365", "366", "367", "368", "369", "370", "371", "372", "373", "374", "375", "376", "377", "378", "379", "380", "381", "382", "383", "384", "385", "386", "387", "388", "389", "390", "391", "392", "393", "394", "395", "396", "397", "398", "399", "400", 
				"401", "402", "403", "404", "405", "406", "407", "408", "409", "410", "411", "412", "413", "414", "415", "416", "417", "418", "419", "420", "421", "422", "423", "424", "425", "426", "427", "428", "429", "430", "431", "432", "433", "434", "435", "436", "437", "438", "439", "440", "441", "442", "443", "444", "445", "446", "447", "448", "449", "450", "451", "452", "453", "454", "455", "456", "457", "458", "459", "460", "461", "462", "463", "464", "465", "466", "467", "468", "469", "470", "471", "472", "473", "474", "475", "476", "477", "478", "479", "480", "481", "482", "483", "484", "485", "486", "487", "488", "489", "490", "491", "492", "493", "494", "495", "496", "497", "498", "499", "500", 
				"501", "502", "503", "504", "505", "506", "507", "508", "509", "510", "511", "512", "513", "514", "515", "516", "517", "518", "519", "520", "521", "522", "523", "524", "525", "526", "527", "528", "529", "530", "531", "532", "533", "534", "535", "536", "537", "538", "539", "540", "541", "542", "543", "544", "545", "546", "547", "548", "549", "550", "551", "552", "553", "554", "555", "556", "557", "558", "559", "560", "561", "562", "563", "564", "565", "566", "567", "568", "569", "570", "571", "572", "573", "574", "575", "576", "577", "578", "579", "580", "581", "582", "583", "584", "585", "586", "587", "588", "589", "590", "591", "592", "593", "594", "595", "596", "597", "598", "599", "600", 
				"601", "602", "603", "604", "605", "606", "607", "608", "609", "610", "611", "612", "613", "614", "615", "616", "617", "618", "619", "620", "621", "622", "623", "624", "625", "626", "627", "628", "629", "630", "631", "632", "633", "634", "635", "636", "637", "638", "639", "640", "641", "642", "643", "644", "645", "646", "647", "648", "649", "650", "651", "652", "653", "654", "655", "656", "657", "658", "659", "660", "661", "662", "663", "664", "665", "666", "667", "668", "669", "670", "671", "672", "673", "674", "675", "676", "677", "678", "679", "680", "681", "682", "683", "684", "685", "686", "687", "688", "689", "690", "691", "692", "693", "694", "695", "696", "697", "698", "699", "700", 
				"701", "702", "703", "704", "705", "706", "707", "708", "709", "710", "711", "712", "713", "714", "715", "716", "717", "718", "719", "720", "721", "722", "723", "724", "725", "726", "727", "728", "729", "730", "731", "732", "733", "734", "735", "736", "737", "738", "739", "740", "741", "742", "743", "744", "745", "746", "747", "748", "749", "750", "751", "752", "753", "754", "755", "756", "757", "758", "759", "760", "761", "762", "763", "764", "765", "766", "767", "768", "769", "770", "771", "772", "773", "774", "775", "776", "777", "778", "779", "780", "781", "782", "783", "784", "785", "786", "787", "788", "789", "790", "791", "792", "793", "794", "795", "796", "797", "798", "799", "800", 
				"801", "802", "803", "804", "805", "806", "807", "808", "809", "810", "811", "812", "813", "814", "815", "816", "817", "818", "819", "820", "821", "822", "823", "824", "825", "826", "827", "828", "829", "830", "831", "832", "833", "834", "835", "836", "837", "838", "839", "840", "841", "842", "843", "844", "845", "846", "847", "848", "849", "850", "851", "852", "853", "854", "855", "856", "857", "858", "859", "860", "861", "862", "863", "864", "865", "866", "867", "868", "869", "870", "871", "872", "873", "874", "875", "876", "877", "878", "879", "880", "881", "882", "883", "884", "885", "886", "887", "888", "889", "890", "891", "892", "893", "894", "895", "896", "897", "898", "899", "900", 
				"901", "902", "903", "904", "905", "906", "907", "908", "909", "910", "911", "912", "913", "914", "915", "916", "917", "918", "919", "920", "921", "922", "923", "924", "925", "926", "927", "928", "929", "930", "931", "932", "933", "934", "935", "936", "937", "938", "939", "940", "941", "942", "943", "944", "945", "946", "947", "948", "949", "950", "951", "952", "953", "954", "955", "956", "957", "958", "959", "960", "961", "962", "963", "964", "965", "966", "967", "968", "969", "970", "971", "972", "973", "974", "975", "976", "977", "978", "979", "980", "981", "982", "983", "984", "985", "986", "987", "988", "989", "990", "991", "992", "993", "994", "995", "996", "997", "998", "999", "1000"}));
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblTaskName)
										.addComponent(lblExecutionType)
										.addComponent(lblFrequency, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
									.addGap(38)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel.createSequentialGroup()
											.addComponent(rdbtnSerial)
											.addGap(4)
											.addComponent(rdbtnParallel))
										.addGroup(gl_panel.createSequentialGroup()
											.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
												.addComponent(taskName, GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
												.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
												.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
											.addGap(131)))))
							.addContainerGap())
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(113)
							.addComponent(btnCreate, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblTestSuite, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(testSuite, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton)
							.addGap(53))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblIteration, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(445, Short.MAX_VALUE))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTaskName)
						.addComponent(taskName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblExecutionType)
						.addComponent(rdbtnParallel)
						.addComponent(rdbtnSerial))
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(27)
							.addComponent(lblFrequency, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(12)
							.addComponent(lblIteration, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTestSuite, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(testSuite, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton))
					.addPreferredGap(ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
					.addComponent(btnCreate)
					.addContainerGap())
		);
		
		JLabel lblHours = new JLabel("Hours");
		lblHours.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblMinutes = new JLabel("Minutes");
		lblMinutes.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblSeconds = new JLabel("Seconds");
		lblSeconds.setFont(new Font("Tahoma", Font.PLAIN, 12));		
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(19)
							.addComponent(lblHours)
							.addGap(28)
							.addComponent(lblMinutes)
							.addGap(18)
							.addComponent(lblSeconds))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addComponent(Hours, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(Minutes, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(Seconds, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(103, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblHours)
						.addComponent(lblMinutes)
						.addComponent(lblSeconds))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(Hours, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(Minutes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(Seconds, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		tree.addMouseListener(new MouseAdapter() {
		      public void mouseClicked(MouseEvent me) {
		        doMouseClicked(me);
		      }
		    });
	}
	
	public static String getUsersHomeDir() {
	    String users_home = System.getProperty("user.home");
	    return users_home.replace("\\", "/"); // to support all platforms.
	}
	
	public static boolean recursiveDelete(File file) {
        //to end the recursive loop
        if (!file.exists())
            return false;
        
        //if directory, go inside and call recursively
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                //call recursively
                recursiveDelete(f);
            }
        }
        //call delete to delete files and empty directory
        return file.delete();
    }
	
	private class Selector implements TreeSelectionListener {
	    public void valueChanged(TreeSelectionEvent event) {
	      /**Object obj = event.getNewLeadSelectionPath().getLastPathComponent();
	      String rootPath = getUsersHomeDir() + File.separator + "ControllerTasks" ;
	      String folderName = obj.toString().split("_")[0];
	      String completePath = rootPath + File.separator + folderName + File.separator + 
	    		  obj.toString();
	      System.out.println(completePath);
	      setTable(completePath);**/
	    }	    
	    
	  }
	
	public void doMouseClicked(MouseEvent event) {
    	if (event.getClickCount() == 2 && !event.isConsumed()) {
    		event.consume();
            System.out.println("Double Click");
            doMouse(event);
            Object obj = tree.getPathForLocation(event.getX(), event.getY()).getLastPathComponent();
  	      	String rootPath = getUsersHomeDir() + File.separator + "ControllerTasks" ;
  	      	String folderName = obj.toString().split("_")[0];
  	      	String completePath = rootPath + File.separator + folderName + File.separator + 
  	    	obj.toString();
  	      	System.out.println(completePath);
  	      	setTable(completePath);
  	      	String memoryPath = rootPath + File.separator + "memory" + File.separator + folderName
  	      			+ File.separator + "mem_"+obj.toString();
  	      setMemoryTable(memoryPath);
  	      String logPath = rootPath + File.separator + "log" + File.separator + folderName
	      			+ File.separator + "log_"+obj.toString();
  	      //setTextArea(logPath);
  	      String MediatorPath = rootPath + File.separator + "MediatorLog" + File.separator + folderName
      			+ File.separator + "log_"+obj.toString();
  	      //SettabbedPane_Mediator_1(MediatorPath);
    	}
        
      }
    void doMouse(MouseEvent me) {
        TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
        System.out.println(tp.getLastPathComponent().toString());
      }
    
    public void SettabbedPane_Mediator_1(String filePath)
	{
    	/**tabbedPane_Mediator_1.removeAll();
    	for(int i=0;i<10;i++)
    	{
    		JPanel panell = new JPanel();
    		tabbedPane_Mediator_1.addTab("Mediatorlog"+i, null, panell, null);
    	}   **/ 	
	}
    public void setMemoryTable(String filePath)
	{
    	/**DefaultTableModel tableModel = (DefaultTableModel)table_1.getModel();
    	int rowCount = tableModel.getRowCount();
    	for(int i=rowCount-1;i>=0;i--)
    	{
    		tableModel.removeRow(i);
    	}
    	try{
    	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
    	    String line = "";
    	    int incr = 1;
    	    while((line = br.readLine())!=null)
    	    {
    	    if(line.contains("----------"))
    	    {
	    	    String data[] = line.split("----------");  
	    	    tableModel = (DefaultTableModel)table_1.getModel();
	    	    long percentage = Long.parseLong(data[3]) / Long.parseLong(data[2]) *100;
	    	    
				String[] tableRow = {Integer.toString(incr),data[1],data[2],data[3],Long.toString(percentage)};
				tableModel.addRow(tableRow);	
				incr = incr +1;
    	    }
    	    }
    	    
    	}catch (IOException ex){
    	         ex.printStackTrace();
    	}**/
    	
	}
    
    public void setTextArea(String filePath)
	{
    	/*txtrControllerLog.setText("");
    	try{
    	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
    	    String line = "";
    	    int incr = 1;
    	    String str = new String();
    	    while((line = br.readLine())!=null)
    	    {
    	    str = str + "\n" + line;
    	    }
    	    txtrControllerLog.setText(str);
    	}catch (IOException ex){
    	         ex.printStackTrace();
    	}*/
    	
	}
    
    public void setTable(String filePath)
	{
    	DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
    	int rowCount = tableModel.getRowCount();
    	for(int i=rowCount-1;i>=0;i--)
    	{
    		tableModel.removeRow(i);
    	}
    	try{
    	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
    	    String line = "";
    	    int incr = 1;
    	    while((line = br.readLine())!=null)
    	    {
    	    if(line.contains("----------"))
    	    {
	    	    String data[] = line.split("----------");  
	    	    tableModel = (DefaultTableModel)table.getModel();
				String[] tableRow = {Integer.toString(incr),data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8]+" ms","Click to view the response...."+data[9]};
				tableModel.addRow(tableRow);	
				incr = incr +1;
    	    }
    	    }
    	    
    	}catch (IOException ex){
    	         ex.printStackTrace();
    	}
    	
	}
    
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}

class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
        	String response = label.replace("Click to view the response....", "");
        	String[] output = response.split("}");
        	String newResponse = new String();
        	for(int i=0;i<output.length;i++)
        	{
        		newResponse = newResponse + output[i] + "}" +"\n";
        	}
        	Notepad frame = new Notepad(newResponse);
			frame.setVisible(true);
        	
            //JOptionPane.showMessageDialog(button, newResponse + ": Ouch!");       
        	}
        isPushed = false;
        return label;
    }
    
    

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}