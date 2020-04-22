package com.gendra.zipCode.service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service ZipCodeServiceImpl Class
 * @author Adolfo Marín
 * @version 1.0
 * @see IZipCodeService
 */

import org.springframework.stereotype.Service;

import com.gendra.zipCode.models.response.ZipCodeResponse;

@Service
public class ZipCodeServiceImpl implements IZipCodeService{
	
	@Autowired
	private ReadZipCodeExcel excel;

	@Override
	public ZipCodeResponse findByCode(String code) {
			return excel.readExcel(code);
	}

}
