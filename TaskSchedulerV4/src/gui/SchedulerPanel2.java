package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Date;
import java.util.Iterator;

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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.ComponentOrientation;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SchedulerPanel2 extends JFrame {
	private JTable table;
	private JTextField textField;

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
	public SchedulerPanel2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 842, 494);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(new Color(130, 135, 144)));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
					.addGap(1))
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
		);
		
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
					.addGap(2)
					.addComponent(tabbedPane)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
						.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
							.addGroup(gl_panel.createSequentialGroup()
								.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)))))
		);
		
		JCheckBoxTree tree = new JCheckBoxTree();
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
			DefaultMutableTreeNode notLoaded = new DefaultMutableTreeNode("Not able to load");
			node.add(notLoaded);
			root.add(node);
		}
		tree.setModel(new DefaultTreeModel(root));
		
		scrollPane_1.setViewportView(tree);
		
		JLabel lblActiveTasks = new JLabel("Active Tasks");
		
		JComboBox comboBox_1 = new JComboBox();
		
		JButton btnStopRunning = new JButton("Stop Running");
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addGap(18)
					.addComponent(lblActiveTasks)
					.addGap(18)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
						.addComponent(btnStopRunning)
						.addComponent(comboBox_1, 0, 153, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_5.setVerticalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addGap(32)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblActiveTasks)
						.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(36)
					.addComponent(btnStopRunning)
					.addContainerGap(44, Short.MAX_VALUE))
		);
		panel_5.setLayout(gl_panel_5);
		
		JLabel lblTaskName = new JLabel("Task Name");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblFrequency = new JLabel("Frequency");
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"15 mins", "30 mins", "1 Hr", "3 Hrs", "6 Hrs", "12 Hrs", "24 Hrs"}));
		
		JButton btnSchedule = new JButton("Schedule");
		
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGap(19)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addComponent(lblTaskName)
						.addComponent(lblFrequency))
					.addGap(18)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addComponent(textField, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
						.addGroup(gl_panel_4.createParallelGroup(Alignment.TRAILING)
							.addComponent(btnSchedule)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGap(28)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTaskName)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFrequency)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btnSchedule)
					.addContainerGap(183, Short.MAX_VALUE))
		);
		panel_4.setLayout(gl_panel_4);
		
		JPanel panel_1 = new JPanel();
		panel_1.setName("");
		tabbedPane.addTab("Controller", null, panel_1, null);
		DefaultListModel<JCheckBox> modelController = new DefaultListModel<JCheckBox>();
		modelController.addElement(new JCheckBox("Get node mount status"));
		modelController.addElement(new JCheckBox("Get Node details"));
		
		JCheckBoxList checkBoxList = new JCheckBoxList(modelController);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addComponent(checkBoxList, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addComponent(checkBoxList, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
		);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Node", null, panel_2, null);
		DefaultListModel<JCheckBox> modelNode = new DefaultListModel<JCheckBox>();
		modelNode.addElement(new JCheckBox("Get Device Capabilities"));
		modelNode.addElement(new JCheckBox("Get LTP Details"));
		JCheckBoxList checkBoxList_1 = new JCheckBoxList(modelNode);
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addComponent(checkBoxList_1, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addComponent(checkBoxList_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
		);
		panel_2.setLayout(gl_panel_2);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("LTPs", null, panel_3, null);
		DefaultListModel<JCheckBox> modelLTPs = new DefaultListModel<JCheckBox>();
		modelLTPs.addElement(new JCheckBox("Retrieve the Air interface configuration"));
		modelLTPs.addElement(new JCheckBox("Retrieve the Air interface capability"));
		modelLTPs.addElement(new JCheckBox("Retrieve the Air interface name parameter"));
		modelLTPs.addElement(new JCheckBox("Retrieve the rx frquency parameter"));
		JCheckBoxList checkBoxList_2 = new JCheckBoxList(modelLTPs);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addComponent(checkBoxList_2, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addComponent(checkBoxList_2, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
		);
		panel_3.setLayout(gl_panel_3);
		panel.setLayout(gl_panel);
		
		table = new JTable();
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Task No", "Task Name", "Testcases", "Time Stamp", "Status"
			}
		));
		scrollPane.setViewportView(table);
		getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnOperations = new JMenu("Operations");
		menuBar.add(mnOperations);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		btnSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
				// validation
				if(textField.getText().isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Please Provide a task name");
				}
				else 
				{
					String taskName = textField.getText();
					String frequency = comboBox.getSelectedItem().toString();
					//String taskNo = "Task00" + TaskDetails.task;
					
					// Get the current selected tab in the tabbed panel
					int CurrentPanel = tabbedPane.getSelectedIndex();
					
					// Check if any item is selected , if not throw a error message
					if(CurrentPanel == 0)
					{
						
						Object[] SelectedCheckboxes = checkBoxList.getSelectedValues();
						String[] SelectedValues = new String[SelectedCheckboxes.length];
						for(int i=0;i< SelectedCheckboxes.length;i++)
						{
							SelectedValues[i]=((JCheckBox)SelectedCheckboxes[i]).getText();
						}
							
						if(SelectedValues.length == 0)
						{
							JOptionPane.showMessageDialog(null, "Please select atleast one test suite to schedule the task");
						}
						else
						{
							/***create the task and maintain it in the repository
							TaskDetails newTask = new TaskDetails(taskNo,taskName,SelectedValues,"","",frequency);
							TaskDetails.taskRepo.put(taskNo,newTask);
							TaskDetails.task++;
							//schedule a task for the granularity
							CustomScheduler newSchedule = new CustomScheduler(taskNo);
							newSchedule.run();
							textField.setText("");
							DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
							//"Task No", "Task Name", "Testcases", "Time Stamp", "Status"
							String[] tableRow = {taskNo,taskName,SelectedCheckboxes.toString(),(new Date()).toString(),"Started"};
							tableModel.addRow(tableRow);****/
							
						}
						
					}
					// if the panel selected is controller , then no need to check anything simple collect the selected items
					
					// if the panel selected is node , then check if any node is selected in the tree panel , if not throw a error message
					
					// if the nodes are selected , then include them 
					
				}
				}catch(Exception e)
				{
					JOptionPane.showMessageDialog(null, "Something went wrong please restart the application");
				}
			}
		});
	}
}
