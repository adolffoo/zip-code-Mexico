package com.gendra.zipCode.controller;

/**
 * ZipCodeController Class
 * @author Adolfo Marín
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.gendra.zipCode.models.response.ZipCodeResponse;
import com.gendra.zipCode.service.IZipCodeService;


@RestController
public class ZipCodeController {
	
	@Autowired
	private IZipCodeService zipCodeService;
	
	
	@GetMapping("/zip-codes/{code}")
	public ResponseEntity<?> getCodes(@PathVariable String code){
		ZipCodeResponse response = new ZipCodeResponse();
		response = zipCodeService.findByCode(code);
		
		if(response.getZip_code() != null) {
			return new ResponseEntity<ZipCodeResponse>(response, HttpStatus.OK);
		}else {
			return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND) ;
		}
	}
}
