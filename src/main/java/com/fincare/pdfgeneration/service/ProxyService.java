package com.fincare.pdfgeneration.service;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="dmsInsertion",url="${dmsUrl:https://uat-apps04.fincarebank.in/DMS/image_operations.php}")
public interface ProxyService {
	
	@RequestMapping(method=RequestMethod.POST)
	public String getDMS(@RequestHeader Map<String ,String>headerMap,@RequestBody String data) ;

}
