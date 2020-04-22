package com.gendra.zipCode.service;

/**
 * Interface IZipCodeService Class
 * @author Adolfo Marín
 * @version 1.0
 */

import com.gendra.zipCode.models.response.ZipCodeResponse;

public interface IZipCodeService {

	public ZipCodeResponse findByCode(String code);
}
