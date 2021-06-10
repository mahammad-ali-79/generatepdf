package com.fincare.pdfgeneration.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.fincare.pdfgeneration.model.User;
import com.lowagie.text.DocumentException;

@Service
public class PDFService {
	
	@Autowired
	TemplateEngine templateEngine;
	@Autowired
	private ProxyService proxyService;
	public void createPDF(User user) throws DocumentException, IOException {
		String dt= user.getVerifyDate();
		 String[] Str=dt.split("/");
		Context context = new Context();
		context.setVariable("name", user.getCustomerName());
		context.setVariable("customerId", user.getCustomerId());
		context.setVariable("depAccNo", user.getDepAccNo());
				context.setVariable("depAmmount", user.getDepAmmount());
				context.setVariable("dateTrans", user.getDateTrans());
				context.setVariable("mode", user.getMode());
				context.setVariable("ackDate", user.getAckDate());
				context.setVariable("agriIncome", user.getAgriIncome());
				context.setVariable("otherIncome", user.getOtherIncome());
				context.setVariable("year", "21" );
				context.setVariable("day", Str[0]);
				context.setVariable("month", Str[1]);
				context.setVariable("creationDate", user.getCreationDate());
		String processhtml=templateEngine.process("index",context);
		OutputStream outputStream=new FileOutputStream("form60.pdf");
		ITextRenderer renderer= new ITextRenderer();
		renderer.setDocumentFromString(processhtml);
		renderer.layout();
		renderer.createPDF(outputStream,false);
		renderer.finishPDF();
		outputStream.close();
	}
	

	public String convertPDFtoBase64(User user) throws IOException, DocumentException {
		
		createPDF(user);
		
		String filepath="form60.pdf";
		
		File fs= new File(filepath);
		byte[] encoded=Files.readAllBytes(Paths.get(fs.getAbsolutePath()));
		byte[] enc= Base64.encodeBase64(encoded);
		String encode=new String(enc,"UTF-8");
		
		return encode;
	}
	
	public String insertPdf(User user) throws IOException, DocumentException {
		

		String req="{\r\n"
				+ "    \"Doc_Type\": \"TDS_CERTIFICATE \",\r\n"
				+ "    \"Operation\": \"DMS_CUSTOMER_PDF\",\r\n"
				+ "    \"Base64_String\":\""+convertPDFtoBase64(user)+"\",\r\n"
				+ "    \"APIAuthentication\": {\r\n"
				+ "        \"APIKey\": \"2018120517071266\",\r\n"
				+ "        \"AppUserID\": \"cashops\",\r\n"
				+ "        \"API_Number\": \"10354_0_1\",\r\n"
				+ "        \"AppPassword\": \"0987ytfdxc90p\",\r\n"
				+ "        \"Environment\": \"D\",\r\n"
				+ "        \"ApplicationID\": \"7\"\r\n"
				+ "    },\r\n"
				+ "    \"Doc_Name_Val_Attr\": {\r\n"
				+ "        \"CIF\": \""+user.getCustomerId()+"\",\r\n"
				+ "        \"TDS_Type\": \"\",\r\n"
				+ "        \"Address Type\": \"Residential Address\",\r\n"
				+ "        \"Document Type\": \"TDS\"\r\n"
				+ "    },\r\n"
				+ "    \"Doc_Reference_Number\": \""+UUID.randomUUID()+"\",\r\n"
				+ "    \"Customer_Reference_Number\": \""+UUID.randomUUID()+"\",\r\n"
				+ "    \"Doc_Part_Reference_Number\": \""+UUID.randomUUID()+"\"\r\n"
				+ "}";
		System.out.println(req);
	Map<String,String>headers=new HashMap<>();
	headers.put("content-type", "application/xml");
		String result= proxyService.getDMS(headers, req);
		System.out.println(result);
		return result;
	}
	
}
