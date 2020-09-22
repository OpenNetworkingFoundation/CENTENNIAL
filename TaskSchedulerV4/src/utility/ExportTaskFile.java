package utility;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ExportTaskFile {
	
	String text = new String();
	File fileName;
	public ExportTaskFile(File fileName)
	{
		this.fileName = fileName;
	}
		
	public void exportToText() {
        
        try { 
            //Choosing Saving Location
            //Set default location to C:\Users\Authentic\Desktop or your preferred location
            JFileChooser textFileChooser = new JFileChooser("C:\\Users\\Authentic\\Desktop");
            //Dialog box title
            textFileChooser.setDialogTitle("Save As ..");
            //Filter only xls, xlsx, xlsm files
            FileNameExtensionFilter fnef = new FileNameExtensionFilter("Files", "txt");
            //Setting extension for selected file names
            textFileChooser.setFileFilter(fnef);
            int chooser = textFileChooser.showSaveDialog(null);
            //Check if save button has been clicked
            if (chooser == JFileChooser.APPROVE_OPTION) {
                System.out.println(textFileChooser.getSelectedFile());                         
            	BufferedWriter writer = new BufferedWriter(new FileWriter(textFileChooser.getSelectedFile()));
            	Scanner scan = new Scanner(fileName.getAbsoluteFile());
            	while(scan.hasNext())
            	{
            		text = text + scan.nextLine() +"\n" ;
            	}
            	scan.close();
                writer.write(text);                
                writer.close();
                JOptionPane.showMessageDialog(null, "Exported Successfully");
            }
 
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex);
        } finally {
            
        }
    } 


}
