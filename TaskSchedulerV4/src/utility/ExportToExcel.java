package utility;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportToExcel {
	
	
		
	public static void exportToExcel(TableModel model) {
		 
        //First Download Apache POI Library For Dealing with excel files.
        //Then add the library to the current project
		
        FileOutputStream excelFos = null;
        Workbook excelJTableExport = null;
        BufferedOutputStream excelBos = null;
        try {
 
            //Choosing Saving Location
            //Set default location to C:\Users\Authentic\Desktop or your preferred location
            JFileChooser excelFileChooser = new JFileChooser("C:\\Users\\Authentic\\Desktop");
            //Dialog box title
            excelFileChooser.setDialogTitle("Save As ..");
            //Filter only xls, xlsx, xlsm files
            FileNameExtensionFilter fnef = new FileNameExtensionFilter("Files", "xls", "xlsx", "xlsm");
            //Setting extension for selected file names
            excelFileChooser.setFileFilter(fnef);
            int chooser = excelFileChooser.showSaveDialog(null);
            //Check if save button has been clicked
            if (chooser == JFileChooser.APPROVE_OPTION) {
                //If button is clicked execute this code
                excelJTableExport = new XSSFWorkbook();
                Sheet excelSheet = excelJTableExport.createSheet();
                //Loop through the jtable columns and rows to get its values
                Row headerRow = excelSheet.createRow(0); //Create row at line 0
                //for(int headings = 0; headings < model.getColumnCount(); headings++){ //For each column
                for(int headings = 0; headings < 5; headings++){ //For each column
                    headerRow.createCell(headings).setCellValue(model.getColumnName(headings));//Write column name
                }
                //for (int i = 0; i < model.getRowCount(); i++) {
                for (int i = 0; i < 5; i++) {
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
