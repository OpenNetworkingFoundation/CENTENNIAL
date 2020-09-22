package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import gui.JCheckBoxTree.CheckedNode;
import operations.LoginToNodes;
import operations.WriteToFile;
import resources.CurrentTestSuite;
import resources.Repository;
import resources.TaskDetails;
import scheduler.CustomScheduler;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Point;
import java.awt.ComponentOrientation;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.JRadioButton;

public class SchedulerPanel extends JFrame {
	private JTable table;
	private JCheckBoxTree tree;
	public static int sNo = 1;
	public static SchedulerPanel reference;
	public static JCheckBoxList airInterfaceLTPCheck;
	public static JTabbedPane LTPTabbedPanel;
	public static JLabel lblSelectLtpsFrom;
	public static ArrayList<String> fetchAll = new ArrayList();
	/**
	 * Launch the application.
	 */
	/**public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SchedulerPanel frame = new SchedulerPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public SchedulerPanel() {
		sNo = 1;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 874, 500);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JPanel panel_4 = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE)
						.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE))
					.addGap(3))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
					.addGap(9)
					.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
		);
		
		JLabel lblNewLabel = new JLabel("Selected TestSuites");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JButton btnCreateTestsuite = new JButton("Create TestSuite");
		btnCreateTestsuite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel defaultModel = (DefaultTableModel)table.getModel();
				String testSuiteContent = new String();
				Set<String> valuesOfTest = new HashSet<String>();
				for(int i=0;i<defaultModel.getRowCount();i++)
				{
					ArrayList<String> values = new ArrayList<String>();
					for(int j=0;j<defaultModel.getColumnCount();j++)
					{
						values.add(defaultModel.getValueAt(i, j).toString());
					}
					CurrentTestSuite.CurrentList.add(new CurrentTestSuite(values.get(0),values.get(1),values.get(2),values.get(3),values.get(4)));
					valuesOfTest.add(values.get(2));
				}
				Iterator iter = valuesOfTest.iterator();
				while(iter.hasNext())
				{
					testSuiteContent = testSuiteContent + iter.next() + ",";
				}
				MainView.testSuite.setText(testSuiteContent);
				Repository.NodeList.clear();
				reference.dispose();
			}
		});
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 676, Short.MAX_VALUE))
					.addGap(37)
					.addComponent(btnCreateTestsuite, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
					.addGap(19))
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(11)
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(49)
							.addComponent(btnCreateTestsuite, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"S.No", "Type", "Test Suite", "Node/LTP details", "Context"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(41);
		table.getColumnModel().getColumn(1).setPreferredWidth(127);
		table.getColumnModel().getColumn(2).setPreferredWidth(217);
		table.getColumnModel().getColumn(3).setPreferredWidth(397);
		scrollPane.setViewportView(table);
		
		JPopupMenu popupMenu = new JPopupMenu();

		popupMenu.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent arg0) {
			}
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
			}
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = table.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), table));
                        if (rowAtPoint > -1) {
                            table.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        }
                    }
                });
			}
		});
		addPopup(table, popupMenu);
		
		JMenuItem mntmDelete = new JMenuItem("Delete");
		mntmDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel model = (DefaultTableModel)table.getModel();
				model.removeRow(table.getSelectedRow());
				JOptionPane.showMessageDialog(null,"Selected row deleted successfully");
			}
		});
		popupMenu.add(mntmDelete);
		panel_4.setLayout(gl_panel_4);
		
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 251, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
							.addGap(11))
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)))
		);
		
		tree = new JCheckBoxTree();
		
		tree.setShowsRootHandles(true);
		tree.setRowHeight(20);
		tree.setLargeModel(true);
		tree.setForeground(Color.BLACK);
		tree.setFont(new Font("Tahoma", Font.PLAIN, 10));
		tree.setFocusTraversalPolicyProvider(true);
		tree.setFocusCycleRoot(true);
		tree.setBorder(new LineBorder(new Color(0, 0, 0)));
		tree.setAutoscrolls(true);
		tree.setAlignmentY(0.0f);
		tree.setAlignmentX(0.0f);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Network");
		Iterator<String> nodeIter = Repository.NodeList.iterator();
		while(nodeIter.hasNext())
		{
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeIter.next());
			root.add(node);
		}
		tree.setModel(new DefaultTreeModel(root));
		
		scrollPane_1.setViewportView(tree);
		
		JPanel panel_1 = new JPanel();
		panel_1.setName("");
		tabbedPane.addTab("Controller", null, panel_1, null);
		DefaultListModel<JCheckBox> modelController = new DefaultListModel<JCheckBox>();
		modelController.addElement(new JCheckBox("Get node mount status"));
		modelController.addElement(new JCheckBox("Get Node details"));
		
		JCheckBoxList checkBoxList = new JCheckBoxList(modelController);
		checkBoxList.setBorder(new LineBorder(new Color(0, 0, 0)));
		checkBoxList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		ButtonGroup radioGroup = new ButtonGroup();
		JButton btnAddTestcase = new JButton("Add TestCase(s)");
		btnAddTestcase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DefaultListModel<JCheckBox> dListModel = (DefaultListModel)checkBoxList.getModel();
				int dListModelSize = dListModel.getSize();
				ArrayList<String> SelectedValues = new ArrayList();
				for(int index=0;index < dListModelSize;index++)
				{
					JCheckBox checkBox = dListModel.get(index);
					if(checkBox.isSelected())
					{
						SelectedValues.add(checkBox.getText());
					}
				}				
				
				if(SelectedValues.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Please select atleast one test suite to schedule the task");
				}
				String context = radioGroup.getSelection().getActionCommand();
				DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
				Iterator<String> valuesIter = SelectedValues.iterator();
				while(valuesIter.hasNext())
				{
				String[] tableRow = {Integer.toString(sNo),"Controller",valuesIter.next(),"Controller",context};
				tableModel.addRow(tableRow);
				sNo++;
				}
		
			}
		});
		
		JRadioButton rdbtnConfig = new JRadioButton("Config");	
		rdbtnConfig.setActionCommand("config");	
		JRadioButton rdbtnNonconfig = new JRadioButton("NonConfig");
		rdbtnNonconfig.setActionCommand("nonconfig");
		JRadioButton rdbtnNone = new JRadioButton("None");
		rdbtnNone.setActionCommand("none");
		radioGroup.add(rdbtnConfig);
		radioGroup.add(rdbtnNonconfig);
		radioGroup.add(rdbtnNone);
		radioGroup.setSelected(rdbtnConfig.getModel(),true);
		
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(418, Short.MAX_VALUE)
					.addComponent(btnAddTestcase)
					.addGap(48))
				.addComponent(checkBoxList, GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(rdbtnConfig)
					.addGap(18)
					.addComponent(rdbtnNonconfig)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnNone, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(346, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnConfig)
						.addComponent(rdbtnNonconfig)
						.addComponent(rdbtnNone))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(checkBoxList, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
					.addComponent(btnAddTestcase)
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Node", null, panel_2, null);
		DefaultListModel<JCheckBox> modelNode = new DefaultListModel<JCheckBox>();
		modelNode.addElement(new JCheckBox("Get Device control-construct"));
		modelNode.addElement(new JCheckBox("Get Device Capabilities"));
		modelNode.addElement(new JCheckBox("Get LTP Details"));
		JCheckBoxList checkBoxList_1 = new JCheckBoxList(modelNode);
		checkBoxList_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		ButtonGroup radioGroup_1 = new ButtonGroup();
		JButton button = new JButton("Add TestCase(s)");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				DefaultListModel<JCheckBox> dListModel = (DefaultListModel)checkBoxList_1.getModel();
				int dListModelSize = dListModel.getSize();
				ArrayList<String> SelectedValues = new ArrayList();
				for(int index=0;index < dListModelSize;index++)
				{
					JCheckBox checkBox = dListModel.get(index);
					if(checkBox.isSelected())
					{
						SelectedValues.add(checkBox.getText());
					}
				}				
				String context = radioGroup_1.getSelection().getActionCommand();
				if(SelectedValues.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Please select atleast one test suite to add the test case");
				}else if(!anyNodeSelected())
				{
					JOptionPane.showMessageDialog(null, "Please select atleast one node from the treeview to add the test case");
				}else
				{				
				DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
				Iterator<String> valuesIter = SelectedValues.iterator();
				ArrayList<String> selectedNodes = new ArrayList();
				ArrayList<String> array = getSelectedNode();
				Iterator<String> arrayIter = array.iterator();
				while(arrayIter.hasNext())
				{
					String selectedString = arrayIter.next();
					if(!selectedString.equalsIgnoreCase("Network"))
					{
						selectedNodes.add(selectedString);
					}					
				}
				while(valuesIter.hasNext())
				{
				String test = valuesIter.next();
				Iterator<String> selectedNodesIter = selectedNodes.iterator();
				while(selectedNodesIter.hasNext())
				{
				String[] tableRow = {Integer.toString(sNo),"Node",test,selectedNodesIter.next(),context};
				tableModel.addRow(tableRow);
				sNo++;
				}
				}
				}			
			}
			
		});
		
		JRadioButton radioButton = new JRadioButton("Config");	
		radioButton.setActionCommand("config");
		JRadioButton radioButton_1 = new JRadioButton("NonConfig");	
		radioButton_1.setActionCommand("nonconfig");		
		JRadioButton rdbtnNone_1 = new JRadioButton("None");		
		rdbtnNone_1.setActionCommand("none");			
		radioGroup_1.add(radioButton);
		radioGroup_1.add(radioButton_1);
		radioGroup_1.add(rdbtnNone_1);
		radioGroup_1.setSelected(radioButton.getModel(), true);
		
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap(418, Short.MAX_VALUE)
					.addComponent(button, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
					.addGap(48))
				.addComponent(checkBoxList_1, GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(radioButton)
					.addGap(18)
					.addComponent(radioButton_1)
					.addGap(18)
					.addComponent(rdbtnNone_1, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(330, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(radioButton)
						.addComponent(radioButton_1)
						.addComponent(rdbtnNone_1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(checkBoxList_1, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
					.addComponent(button)
					.addContainerGap())
		);
		panel_2.setLayout(gl_panel_2);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("LTPs", null, panel_3, null);
		DefaultListModel<JCheckBox> modelLTPs = new DefaultListModel<JCheckBox>();
		modelLTPs.addElement(new JCheckBox("Retrieve the Air interface configuration"));
		modelLTPs.addElement(new JCheckBox("Retrieve the Air interface capability"));
		modelLTPs.addElement(new JCheckBox("Retrieve the Air interface name parameter"));
		modelLTPs.addElement(new JCheckBox("Retrieve the rx frquency parameter"));
		
		LTPTabbedPanel= new JTabbedPane(JTabbedPane.TOP);
		LTPTabbedPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JRadioButton radioButton_2 = new JRadioButton("Config");	
		radioButton_2.setActionCommand("config");	
		JRadioButton radioButton_3 = new JRadioButton("NonConfig");
		radioButton_3.setActionCommand("nonconfig");
		ButtonGroup radioGroup_2 = new ButtonGroup();	
		JRadioButton radioButton_4 = new JRadioButton("None");
		radioButton_4.setActionCommand("none");
		radioGroup_2.add(radioButton_2);
		radioGroup_2.add(radioButton_3);
		radioGroup_2.add(radioButton_4);
		radioGroup_2.setSelected(radioButton_2.getModel(),true);	
		
		JButton btnFetchLtps = new JButton("Fetch LTPs");
		
		btnFetchLtps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				if(!anyNodeSelected())
				{
					JOptionPane.showMessageDialog(null, "Please select atleast one node from the treeview to add the test case");
				}else
				{				
					if(!new File(getUsersHomeDir()+ File.separator+"PerformanceTool").exists())
				    {
				    	new File(getUsersHomeDir()+ File.separator+"PerformanceTool").mkdir();
				    	System.out.println("Directory is created!");
				    }
				ArrayList<String> selectedNodes = new ArrayList();
				ArrayList<String> array = getSelectedNode();
				Iterator<String> arrayIter = array.iterator();
				while(arrayIter.hasNext())
				{
					String selectedString = arrayIter.next();
					if(!selectedString.equalsIgnoreCase("Network"))
					{
						selectedNodes.add(selectedString);
					}					
				}
				if(SchedulerPanel.LTPTabbedPanel.isShowing())
                {
                Progress wait = new Progress("fetching all LTPs");
    			SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {	
    				@Override
				    protected Void doInBackground() throws Exception {
    					//Read from the file and store the already loaded nodes
    					Map<String,Set<String>> NodeDetails = new HashMap<String,Set<String>>();
    					File fetch = new File(getUsersHomeDir()+ File.separator+"PerformanceTool"+ File.separator +"Controller_LTP_Details");
    					if(fetch.exists())
    					{
    						Scanner scan = new Scanner(fetch);
    						while(scan.hasNext())
    						{
    							String fullContent = scan.next();
    							String nodeDet = fullContent.split(":::")[0];
    							String details = fullContent.split(":::")[1];
    							String LPdetails = fullContent.split(":::")[2];
    							String LTPLPvalues = fullContent.split(":::")[3];
    							Map<String,String> value = new HashMap<String,String>();
    							value.put(details, LPdetails);
    							Map<String,String> LTPLPmap = new HashMap();
    							LTPLPmap.put(details,LTPLPvalues);
    							if(NodeDetails.containsKey(nodeDet))
    							{
    								Set existing = NodeDetails.get(nodeDet);
    								existing.add(nodeDet+":::"+details);
    								Map lpmap = Repository.MapLogicalProtocolMap.get(nodeDet);
    								lpmap.put(details, LPdetails);
    								Map existingmap = Repository.MapLTP_LPMap.get(nodeDet);
    								existingmap.put(details,LTPLPvalues);
    							}else
    							{
    								Set set = new HashSet();
    								set.add(nodeDet+":::"+details);
    								NodeDetails.put(nodeDet, set); 
    								Repository.MapLogicalProtocolMap.put(nodeDet, value);
    								Repository.MapLTP_LPMap.put(nodeDet,LTPLPmap);    							
    							}
    						}
    						scan.close();
    					}
    					//If its not there in the file , then load it 
    					Iterator<String> selectArrayIter = selectedNodes.iterator();
    					fetchAll.clear();
    					while(selectArrayIter.hasNext())
    					{
    					String selectedNode = selectArrayIter.next();
    					if(NodeDetails.containsKey(selectedNode))
    					{
    						Set<String> LTPlist = NodeDetails.get(selectedNode);
    						Iterator<String> iter = LTPlist.iterator();
    						while(iter.hasNext())
    						{
    						fetchAll.add(iter.next());
    						}
    					}else
    					{
    					System.out.println(getUsersHomeDir()+ File.separator+"PerformanceTool"+ File.separator +"Controller_LTP_Details");
    					WriteToFile writer = new WriteToFile(getUsersHomeDir()+ File.separator+"PerformanceTool"+ File.separator +"Controller_LTP_Details");
    					new LoginToNodes(selectedNode,"fetchall");
    					if(Repository.MapLogicalProtocolMap.containsKey(selectedNode))
    					{
    						Map<String,String> LTPs = Repository.MapLogicalProtocolMap.get(selectedNode);
    						Set<Map.Entry<String, String>> entryMap = LTPs.entrySet();
    				    	Iterator<Map.Entry<String, String>> entryMapIterator = entryMap.iterator();
    				    	while(entryMapIterator.hasNext())
    				    	{
    				    	Map.Entry<String, String> entry = entryMapIterator.next();
    			    		if(entry.getValue().contains("air-interface"))
    			    		{
    			    			fetchAll.add(selectedNode + ":::" + entry.getKey());
    			    			String LPValue = Repository.MapLTP_LPMap.get(selectedNode).get(entry.getKey());
    			    			writer.write(selectedNode + ":::" + entry.getKey()+ ":::" +entry.getValue()+ ":::" + LPValue + "\n");
    			    		}
    				    	}    			    	
    					}
    					writer.close();
    					}
    					}
    					ListModel<JCheckBox> sample = SchedulerPanel.airInterfaceLTPCheck.getModel();
    			    	DefaultListModel<JCheckBox> model = (DefaultListModel)sample;
    			    	model.removeAllElements();
    			    	Iterator<String> fetchedList = fetchAll.iterator();
    			    	while(fetchedList.hasNext())
    			    	{
    			    		String entry = fetchedList.next();
    			    		model.addElement(new JCheckBox(entry));
    			    	} 
    					

    					wait.close();
				        return null;
    				 }
				};
				mySwingWorker.execute();
				wait.makeButtonWait("fetching LTPs", e);                
                }
				}			
			}
			
			protected void updateLTPview(String LastPath)
		    {
		    	SchedulerPanel.lblSelectLtpsFrom.setText("Select LTPs :"+LastPath);
		    	new LoginToNodes(LastPath);
		    	ListModel<JCheckBox> sample = SchedulerPanel.airInterfaceLTPCheck.getModel();
		    	DefaultListModel<JCheckBox> model = (DefaultListModel)sample;
		    	model.removeAllElements();
		    	Map<String,String> LTPs = Repository.MapLogicalProtocolMap.get(LastPath);
		    	Set<Map.Entry<String, String>> entryMap = LTPs.entrySet();
		    	Iterator<Map.Entry<String, String>> entryMapIterator = entryMap.iterator();
		    	   	
		    }
			
		});
		
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(radioButton_2)
							.addGap(10)
							.addComponent(radioButton_3)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(radioButton_4, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btnFetchLtps))
						.addComponent(LTPTabbedPanel, GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
						.addComponent(radioButton_2)
						.addComponent(radioButton_3)
						.addComponent(radioButton_4)
						.addComponent(btnFetchLtps))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(LTPTabbedPanel, GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
					.addGap(18))
		);
		
		JPanel airInterfaceLTP = new JPanel();
		LTPTabbedPanel.addTab("AirInterface", null, airInterfaceLTP, null);
		DefaultListModel<JCheckBox> modelairInterfaceLTPCheck = new DefaultListModel<JCheckBox>();
		
		DefaultListModel<JCheckBox> modelAirInterface = new DefaultListModel<JCheckBox>();
		modelAirInterface.addElement(new JCheckBox("Get AirInterface Capability"));
		modelAirInterface.addElement(new JCheckBox("Get AirInterface Configuration"));
		modelAirInterface.addElement(new JCheckBox("Get attribute air-interface-name"));
		modelAirInterface.addElement(new JCheckBox("Get attribute tx-frequency"));
		modelAirInterface.addElement(new JCheckBox("Get attribute rx-frequency"));
		modelAirInterface.addElement(new JCheckBox("Get attribute power-is-on"));
		modelAirInterface.addElement(new JCheckBox("Get attribute modulation-is-on"));
		modelAirInterface.addElement(new JCheckBox("Get attribute auto-freq-select-range"));
		modelAirInterface.addElement(new JCheckBox("Get attribute atpc-thresh-lower"));
		modelAirInterface.addElement(new JCheckBox("Get attribute performance-monitoring-is-on"));
		modelAirInterface.addElement(new JCheckBox("Get attribute maintenance-timer"));
		modelAirInterface.addElement(new JCheckBox("Get attribute cryptographic-key"));
		
		
		JCheckBoxList airInterfaceTestSuites = new JCheckBoxList(modelAirInterface);
		airInterfaceTestSuites.setValueIsAdjusting(true);
		airInterfaceTestSuites.setVisibleRowCount(20);
		airInterfaceTestSuites.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JButton btnAddAirTestcases = new JButton("Add TestCase(s)");
		btnAddAirTestcases.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultListModel<JCheckBox> dListModel = (DefaultListModel)airInterfaceTestSuites.getModel();
				int dListModelSize = dListModel.getSize();
				ArrayList<String> SelectedValues = new ArrayList();
				for(int index=0;index < dListModelSize;index++)
				{
					JCheckBox checkBox = dListModel.get(index);
					if(checkBox.isSelected())
					{
						SelectedValues.add(checkBox.getText());
					}
				}

				DefaultListModel<JCheckBox> LTPListModels = (DefaultListModel)airInterfaceLTPCheck.getModel();
				int LTPListModelSize = LTPListModels.getSize();
				ArrayList<String> LTPSelectedValues = new ArrayList();
				for(int index=0;index < LTPListModelSize;index++)
				{
					JCheckBox checkBox = LTPListModels.get(index);
					if(checkBox.isSelected())
					{
						LTPSelectedValues.add(checkBox.getText());
					}
				}
				String context = radioGroup_2.getSelection().getActionCommand();
				if(SelectedValues.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Please select atleast one test suite to add the test case");
				}else if(LTPSelectedValues.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Please click a node from the treeview and select atleast one LTP");
				}else
				{				
				DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
				Iterator<String> valuesIter = SelectedValues.iterator();
				while(valuesIter.hasNext())
				{
				String test = valuesIter.next();
				Iterator<String> LTPSelectedValuesIter = LTPSelectedValues.iterator();
				while(LTPSelectedValuesIter.hasNext())
				{
				String SelectedLTs = LTPSelectedValuesIter.next();
				if(SelectedLTs.contains(":::"))
				{
				String[] tableRow = {Integer.toString(sNo),"LTP",test,SelectedLTs.split(":::")[0]+":"+SelectedLTs.split(":::")[1],context};
				tableModel.addRow(tableRow);
				sNo++;	
				}
				else
				{
				String[] tableRow = {Integer.toString(sNo),"LTP",test,lblSelectLtpsFrom.getText().split(":")[1]+":"+SelectedLTs,context};
				tableModel.addRow(tableRow);
				sNo++;
				}
				}
				}
				}
			}
		});
		
		lblSelectLtpsFrom= new JLabel("Select LTPs");
		
		JLabel lblSelectTestsuitsFrom = new JLabel("Select Testsuits");
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		JCheckBox chckbxSelectAll = new JCheckBox("Select All");
		chckbxSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(chckbxSelectAll.isSelected())
				{
				int size = airInterfaceLTPCheck.getModel().getSize();
				for(int i=0;i<size;i++)
				{
					JCheckBox box = airInterfaceLTPCheck.getModel().getElementAt(i);
					box.setSelected(true);
				}
				}else
				{
				int size = airInterfaceLTPCheck.getModel().getSize();
				for(int i=0;i<size;i++)
				{
					JCheckBox box = airInterfaceLTPCheck.getModel().getElementAt(i);
					box.setSelected(false);
				}
				}
				airInterfaceLTPCheck.repaint();
			}
		});
		GroupLayout gl_airInterfaceLTP = new GroupLayout(airInterfaceLTP);
		gl_airInterfaceLTP.setHorizontalGroup(
			gl_airInterfaceLTP.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_airInterfaceLTP.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_airInterfaceLTP.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_airInterfaceLTP.createSequentialGroup()
							.addComponent(lblSelectLtpsFrom, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(chckbxSelectAll)
							.addPreferredGap(ComponentPlacement.RELATED, 188, Short.MAX_VALUE)
							.addComponent(lblSelectTestsuitsFrom)
							.addGap(108))
						.addComponent(btnAddAirTestcases, Alignment.TRAILING)
						.addGroup(gl_airInterfaceLTP.createSequentialGroup()
							.addGap(2)
							.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(airInterfaceTestSuites, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_airInterfaceLTP.setVerticalGroup(
			gl_airInterfaceLTP.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_airInterfaceLTP.createSequentialGroup()
					.addGroup(gl_airInterfaceLTP.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_airInterfaceLTP.createSequentialGroup()
							.addContainerGap(39, Short.MAX_VALUE)
							.addComponent(airInterfaceTestSuites, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_airInterfaceLTP.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_airInterfaceLTP.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblSelectLtpsFrom)
								.addComponent(lblSelectTestsuitsFrom)
								.addComponent(chckbxSelectAll))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnAddAirTestcases)
					.addContainerGap())
		);
		airInterfaceLTPCheck = new JCheckBoxList(modelairInterfaceLTPCheck);
		scrollPane_2.setViewportView(airInterfaceLTPCheck);
		airInterfaceLTPCheck.setVisibleRowCount(100);
		airInterfaceLTPCheck.setBorder(new LineBorder(new Color(0, 0, 0)));
		airInterfaceLTP.setLayout(gl_airInterfaceLTP);
		
		JPanel panel_6 = new JPanel();
		LTPTabbedPanel.addTab("Ethernet Container", null, panel_6, null);
		
		JPanel panel_7 = new JPanel();
		LTPTabbedPanel.addTab("Hybrid Microwave", null, panel_7, null);
		panel_3.setLayout(gl_panel_3);
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnOperations = new JMenu("Operations");
		menuBar.add(mnOperations);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
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
	
	private ArrayList<String> getSelectedNode()
	{
	 ArrayList<String> list = new ArrayList();
	 HashMap<TreePath, JCheckBoxTree.CheckedNode> map = tree.nodesCheckingState;
	 Set<Entry<TreePath, JCheckBoxTree.CheckedNode>> entrySet = map.entrySet();
	 Iterator<Entry<TreePath, JCheckBoxTree.CheckedNode>> entryIter = entrySet.iterator();
	 while(entryIter.hasNext())
	 {
		 Entry<TreePath, JCheckBoxTree.CheckedNode> entry = entryIter.next();
		 if(entry.getValue().isSelected)
		 {
			 list.add(((DefaultMutableTreeNode)entry.getKey().getLastPathComponent()).getUserObject().toString());			 
		 }
	 }
	 return list;
	}
	
	private boolean anyNodeSelected()
	{
	 HashMap<TreePath, JCheckBoxTree.CheckedNode> map = tree.nodesCheckingState;
	 Set<Entry<TreePath, JCheckBoxTree.CheckedNode>> entrySet = map.entrySet();
	 Iterator<Entry<TreePath, JCheckBoxTree.CheckedNode>> entryIter = entrySet.iterator();
	 while(entryIter.hasNext())
	 {
		 Entry<TreePath, JCheckBoxTree.CheckedNode> entry = entryIter.next();
		 if(entry.getValue().isSelected)
		 {
			 return true;
		 }
	 }
	 return false;
	}
	
	public static String getUsersHomeDir() {
	    String users_home = System.getProperty("user.home");
	    return users_home.replace("\\", "/"); // to support all platforms.
	}
}
