package com.cd.enquiry.msvc.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.util.ByteArrayDataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cd.enquiry.msvc.config.DbChannel;
import com.cd.enquiry.msvc.constants.CDConstants;
import com.cd.enquiry.msvc.domain.SmsInterest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExcelForSmsUtil {

	@Autowired
	DbChannel dbChannel;
	
	@Autowired
	CDMailService cdMailService;
	
	@Value("${enquiry.mail.to}")
	private String to;

	public String readFileMultiPart(MultipartFile file) throws IOException {
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
	
	public void sendMailForInterestedCandidates(LocalDateTime date) throws IOException {
		List<SmsInterest> interestList = dbChannel.getSmsIntrestAfter(date);
		MailObj mobj = new MailObj();
		mobj.setContent("Please find attached list of students/parents who enquired for CosDelta Academy after " + date);
		mobj.setTo(to);
		mobj.setSubject("List of Interested Students through sms");
		mobj.setDataSource(createExcel(interestList));
		try {
			cdMailService.sendSimpleMessage(mobj);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	private DataSource createExcel(List<SmsInterest> interestList) {
		Workbook xlsFile = new HSSFWorkbook(); // create a workbook
		Sheet sheet1 = xlsFile.createSheet("enquired_students"); // add a sheet to your workbook
		Row header = sheet1.createRow(0);

		header.createCell(0).setCellValue("NAME");
		header.createCell(1).setCellValue("CONTACT");
		header.createCell(2).setCellValue("COURSE");
		header.createCell(3).setCellValue("CLASS");
		header.createCell(4).setCellValue("SUBJECT");
		header.createCell(5).setCellValue("TYPE");
		header.createCell(6).setCellValue("DATE");
		int i = 1;
		for (SmsInterest s : interestList) {
			Row row = sheet1.createRow(i++); // create a new row in your sheet
			row.createCell(0).setCellValue(s.getName());
			row.createCell(1).setCellValue(s.getPhoneNum());
			row.createCell(2).setCellValue(s.getCName());
			row.createCell(3).setCellValue(s.getLName());
			row.createCell(4).setCellValue(s.getSName());
			row.createCell(5).setCellValue(s.getType());
			row.createCell(6).setCellValue(s.getDate().toString());
		}
		autoSizeColumnForSheet(sheet1, 7);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			xlsFile.write(bos); // write excel data to a byte array
			bos.close();
			xlsFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DataSource fds = new ByteArrayDataSource(bos.toByteArray(), "application/vnd.ms-excel");
		return fds;
	}

	private void autoSizeColumnForSheet(Sheet sheet1, int autoSizeColumnTill) {
		for (int i = 0; i < autoSizeColumnTill; i++) {
			sheet1.autoSizeColumn(i);
		}
	}

}
