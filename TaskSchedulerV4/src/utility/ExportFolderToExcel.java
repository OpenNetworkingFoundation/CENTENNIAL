package utility;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportFolderToExcel {	
	
	public static boolean fullreport = false;		
	    
		public static TableModel readTheEntireFolder(String directoryPath)
		{
			try
			{
			JTable table = new JTable(new DefaultTableModel(
					new Object[][] {
					},
					new String[] {
						"Iteration" , "No","Details","TestSuite","Content","URL Details", "Start Time", "End Time", "Status", "Response Time"
					}
				));
			DefaultTableModel model = (DefaultTableModel)table.getModel();				
			File folder = new File(directoryPath);
			if(folder.isDirectory())
			{
			File[] listOfFiles = folder.listFiles();			
			for (int i = 0; i < listOfFiles.length; i++) 
			{
				if (listOfFiles[i].isFile()) 
				{
				System.out.println("File " + listOfFiles[i].getName());
				String fileName = directoryPath + File.separator + listOfFiles[i].getName();
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
	    	    String line = "";
	    	    int incr = 1;
	    	    while((line = br.readLine())!=null)
	    	    {
	    	    if(line.contains("----------"))
	    	    {
	    	    	int it = i + 1;
	    	    	String data[] = line.split("----------");  
	    	    	System.out.println(line);
	    	    	String[] tableRow = {"Iteration-"+it,Integer.toString(incr),data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8]+" ms"};
	    	    	model.addRow(tableRow);				
	    	    	incr = incr +1;
	    	    }
	    	    }
				} else if (listOfFiles[i].isDirectory()) 
				{
					fullreport = true;					
					//do nothing				
				}
			}
			}else
			{
				//C:/Users/Siva Arun M\ControllerTasks\testfinal2_18_06_2020_20_11_45
				String pattern = Pattern.quote(File.separator);
				String basePath = directoryPath.split(pattern)[0] + File.separator;
				String baseDirectory = directoryPath.split(pattern)[1] + File.separator;
				String dir = directoryPath.split(pattern)[2].split("_")[0] + File.separator;
				String fle = directoryPath.split(pattern)[2];
				String folderPath = basePath + baseDirectory + dir + fle;
				
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(folderPath))));
	    	    String line = "";
	    	    while((line = br.readLine())!=null)
	    	    {
	    	    String data[] = line.split("----------");  
	    	    String[] tableRow = {data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8]+" ms"};	    	    
	    	    model.addRow(tableRow);				
	    	    }
			}
			return table.getModel();
			}catch(Exception ex)
			{
				JOptionPane.showMessageDialog(null, "Problem in generating the excelsheet");
				System.out.println(ex);
			}
			return new DefaultTableModel();
		}
		
		public static void exportToExcel(String directoryPath) {			 
	        
	        FileOutputStream excelFos = null;
	        Workbook excelJTableExport = null;
	        BufferedOutputStream excelBos = null;
	        try {
	 
	            JFileChooser excelFileChooser = new JFileChooser("C:\\Users\\Authentic\\Desktop");
	            excelFileChooser.setDialogTitle("Save As ..");
	            FileNameExtensionFilter fnef = new FileNameExtensionFilter("Files", "xls", "xlsx", "xlsm");
	            excelFileChooser.setFileFilter(fnef);
	            int chooser = excelFileChooser.showSaveDialog(null);
	            if (chooser == JFileChooser.APPROVE_OPTION) {
	                excelJTableExport = new XSSFWorkbook();
	                Sheet excelSheet = excelJTableExport.createSheet();
	                Row headerRow = excelSheet.createRow(0); 
	                TableModel model = readTheEntireFolder(directoryPath);	                
	                for(int headings = 0; headings < model.getColumnCount(); headings++){ //For each column
	                    headerRow.createCell(headings).setCellValue(model.getColumnName(headings));//Write column name
	                }
	                for (int i = 0; i < model.getRowCount(); i++) {
	                	Row excelRow = excelSheet.createRow(i+1);
	                    for (int j = 0; j < model.getColumnCount(); j++) {
	                        excelRow.createCell(j).setCellValue(model.getValueAt(i, j).toString());
	                    }
	                }                                
	                
	                excelFos = new FileOutputStream(excelFileChooser.getSelectedFile() + ".xlsx");
	                excelJTableExport.write(excelFos);
	                JOptionPane.showMessageDialog(null, "Exported Successfully");
	            }
	 
	        } catch (FileNotFoundException ex) {
	            JOptionPane.showMessageDialog(null, ex);
	        } catch (IOException ex) {
	            JOptionPane.showMessageDialog(null, ex);
	        } finally {
	            try {
	                if (excelFos != null) {
	                    excelFos.close();
	                }
	                if (excelBos != null) {
	                    excelBos.close();
	                }
	                if (excelJTableExport != null) {
	                    excelJTableExport.close();
	                }
	            } catch (IOException ex) {
	                JOptionPane.showMessageDialog(null, ex);
	            }
	        }
	    } 


}
