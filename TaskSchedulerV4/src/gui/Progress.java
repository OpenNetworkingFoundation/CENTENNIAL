package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Progress {

	public JDialog dialog;
	public String nodeName;
	
	public Progress(String nodeName)
	{
		this.nodeName = nodeName;
	}

	public void makeWait(String msg, MouseEvent evt) {

	    Window win = SwingUtilities.getWindowAncestor((JCheckBoxTree) evt.getSource());
	    dialog = new JDialog(win, msg, JDialog.ModalityType.APPLICATION_MODAL);

	    JProgressBar progressBar = new JProgressBar();
	    progressBar.setIndeterminate(true);
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.add(progressBar, BorderLayout.CENTER);
	    panel.add(new JLabel("Loading the LTPs of "+nodeName+"...."), BorderLayout.PAGE_START);
	    dialog.getContentPane().add(panel);
	    dialog.pack();
	    dialog.setLocationRelativeTo(win);
	    dialog.setVisible(true);
	    dialog.setDefaultCloseOperation(dialog.DO_NOTHING_ON_CLOSE);
	   }
	
	public void makeButtonWait(String msg, ActionEvent evt) {

	    Window win = SwingUtilities.getWindowAncestor((AbstractButton) evt.getSource());
	    dialog = new JDialog(win, msg, JDialog.ModalityType.APPLICATION_MODAL);

	    JProgressBar progressBar = new JProgressBar();
	    progressBar.setIndeterminate(true);
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.add(progressBar, BorderLayout.CENTER);
	    panel.add(new JLabel("Connecting to the controller...."), BorderLayout.PAGE_START);
	    dialog.getContentPane().add(panel);
	    dialog.pack();
	    dialog.setLocationRelativeTo(win);
	    dialog.setVisible(true);
	    dialog.setDefaultCloseOperation(dialog.DO_NOTHING_ON_CLOSE);
	   }
	
	public void makeMenuWait(String msg, ActionEvent evt) {

	    Window win = SwingUtilities.getWindowAncestor((AbstractButton) evt.getSource());
	    dialog = new JDialog(win, msg, JDialog.ModalityType.APPLICATION_MODAL);

	    JProgressBar progressBar = new JProgressBar();
	    progressBar.setIndeterminate(true);
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.add(progressBar, BorderLayout.CENTER);
	    panel.add(new JLabel("Exporting ...."), BorderLayout.PAGE_START);
	    dialog.getContentPane().add(panel);
	    dialog.pack();
	    dialog.setLocationRelativeTo(win);
	    dialog.setVisible(true);
	    dialog.setDefaultCloseOperation(dialog.DO_NOTHING_ON_CLOSE);
	   }


	   public void close() {
	       dialog.dispose();
	   }
	}