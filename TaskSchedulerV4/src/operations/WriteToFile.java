package operations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {
	
	File file;
	FileWriter myWriter;
	
	public WriteToFile(String fileName)
	{
		try
		{
		file = new File(fileName);
		myWriter = new FileWriter(file.getAbsoluteFile(),true);
		}catch(Exception ex)
		{
			
		}
	}
	
	public void write(String line)
	{
		try {
			myWriter.write(line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		try {
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
