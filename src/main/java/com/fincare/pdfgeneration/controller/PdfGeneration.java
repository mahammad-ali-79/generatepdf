package com.fincare.pdfgeneration.controller;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fincare.pdfgeneration.model.User;
import com.fincare.pdfgeneration.service.PDFService;
import com.lowagie.text.DocumentException;

@RestController 
@RequestMapping("/form")
public class PdfGeneration   
{    
	@Autowired
	private PDFService pdfService;
	@PostMapping(path = "/pdf")
	  
	    public String getPdf(@RequestBody User user) throws DocumentException, IOException {
	       
	    String resp= pdfService.insertPdf(user);
	    return resp;
	        
	    }
}   
	    
	    
	    