package help.helper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFile {
	private static final String PATH = "C:\\Users\\jjanku\\Desktop\\cert.xlsx";

	public static List<Certification> getCertificationList() throws Exception{
		List<Certification> toReturn = new ArrayList<>();
		
		FileInputStream input = new FileInputStream(PATH);
		
		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook(input);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row;
		XSSFCell cell;
		
		int rows = sheet.getPhysicalNumberOfRows();
		int cols = 0;
		int tmp = 0;
		
		 for(int i = 0; i < 10 || i < rows; i++) {
		        row = sheet.getRow(i);
		        if(row != null) {
		            tmp = sheet.getRow(i).getPhysicalNumberOfCells();
		            if(tmp > cols) cols = tmp;
		        }
		    }
		 
		  for(int r = 1; r < rows; r++) {
		        row = sheet.getRow(r);
		        if(row != null) {
		        	List<String> values = new ArrayList<>();
		            for(int c = 0; c < cols; c++) {
		                cell = row.getCell((short)c);
		                if(cell != null) {
		                try {
		                	 values.add(cell.getStringCellValue());
		                } catch(IllegalStateException e) {
		                	values.add(new Double(cell.getNumericCellValue()).toString());
		                	
		                }
		                  
		                }
		            }

		            if (values.size() != 0) {
		            	Double hours = null;
		            	String desc = "";
		            	try {
		            		hours = Double.parseDouble(values.get(1));
		            		System.out.println("GOT "+hours);
		            		desc = values.get(2);
		            	} catch(NumberFormatException  e) {
		            		
		            	} catch(  IndexOutOfBoundsException e) {
		            		
		            	}
		            	          
		            	Certification certification = new Certification(values.get(0),hours != null ? hours.intValue() : null,desc);
		            	toReturn.add(certification);
		            }
		            	

		        }
		    }
		  return toReturn;
	}
	
}
