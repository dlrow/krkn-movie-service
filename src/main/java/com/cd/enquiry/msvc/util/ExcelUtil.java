package com.cd.enquiry.msvc.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.cd.enquiry.msvc.domain.Student;

@Service
public class ExcelUtil {

	public DataSource createExcel(List<Student> studentList) {
		Workbook xlsFile = new HSSFWorkbook(); // create a workbook
		Sheet sheet1 = xlsFile.createSheet("enquired_students"); // add a sheet to your workbook
		Row header = sheet1.createRow(0);

		header.createCell(0).setCellValue("NAME");
		header.createCell(1).setCellValue("Email");
		header.createCell(2).setCellValue("Contact");
		header.createCell(3).setCellValue("Enquired Date");
		header.createCell(3).setCellValue("Enquired For");
		int i = 1;
		for (Student s : studentList) {
			Row row = sheet1.createRow(i++); // create a new row in your sheet
			row.createCell(0).setCellValue(s.getName());
			row.createCell(1).setCellValue(s.getEmail());
			row.createCell(2).setCellValue(s.getContact());
			row.createCell(3).setCellValue(String.valueOf(s.getEnquiryDate()));
			row.createCell(4).setCellValue(s.getCourse());
		}
		autoSizeColumnForSheet(sheet1, 5);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			xlsFile.write(bos); // write excel data to a byte array
			bos.close();
			xlsFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Now use your ByteArrayDataSource as
		DataSource fds = new ByteArrayDataSource(bos.toByteArray(), "application/vnd.ms-excel");
		return fds;
	}

	private void autoSizeColumnForSheet(Sheet sheet1, int autoSizeColumnTill) {
		for (int i = 0; i < autoSizeColumnTill; i++) {
			sheet1.autoSizeColumn(i);
		}
	}

}
