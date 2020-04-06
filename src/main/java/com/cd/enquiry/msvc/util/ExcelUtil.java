package com.cd.enquiry.msvc.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cd.enquiry.msvc.constants.CDConstants;
import com.cd.enquiry.msvc.domain.Student;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExcelUtil {

	public String getMobileNumbersFromExcelSheet(MultipartFile file) throws IOException {
		InputStream inputStream = file.getInputStream();

		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);
		if (sheet.getLastRowNum() == 0) {
			workbook.close();
			return "The excel seems to be blank!! Please have a look at the uploaded excel.";
		}

		Row headerRow = sheet.getRow(0);
		int contactColumn = getContactColumnIndex(headerRow);

		List<String> phoneNumbers = new ArrayList<>();
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row currRow = sheet.getRow(i);
			String ph = getCellValue(currRow.getCell(contactColumn));
			if (isInValidPhoneNumber(ph)) {
				log.info("invalid phone number at row ", i + 1, "  phone ", ph);
				continue;
			}
			phoneNumbers.add(ph);
		}
		// inputStream.close();
		workbook.close();
		return phoneNumbers.toString();
	}

	private int getContactColumnIndex(Row headerRow) {
		for (int i = 0; i < headerRow.getLastCellNum(); i++) {
			String cv = getCellValue(headerRow.getCell(i));
			if (cv.equalsIgnoreCase(CDConstants.CONTACT))
				return i;
		}
		return 1;
	}

	private boolean isInValidPhoneNumber(String ph) {
		for (int i = 1; i < ph.length(); i++) {
			if (ph.charAt(i) > '9' || ph.charAt(i) < '0')
				return false;
		}
		return ph.length() < 10;
	}

	private static String getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
		switch (cell.getCellTypeEnum()) {
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case STRING:
			return String.valueOf(cell.getRichStringCellValue().getString());
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return String.valueOf(cell.getDateCellValue());
			} else {
				return String.valueOf(cell.getNumericCellValue());
			}
		case FORMULA:
			return String.valueOf(cell.getCellFormula());
		case BLANK:
			return "";
		default:
			return "";
		}
	}

	public DataSource createExcelToMailListOfEnquiry(List<Student> studentList) {
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
