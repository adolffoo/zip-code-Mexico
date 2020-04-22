package com.gendra.zipCode.service;

/**
 * Service ReadZipCodeExcel Class
 * @author Adolfo Marín
 * @version 1.0
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.gendra.zipCode.models.response.SettlementsResponse;
import com.gendra.zipCode.models.response.ZipCodeResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

@Service
public class ReadZipCodeExcel {
	
	ZipCodeResponse response = new ZipCodeResponse();
	int indiceDatos = 0;
	Row filaDatos = null;
	Cell celda = null;
	Boolean flag = false;
	Boolean flagAsenta = false;
	Boolean flagTipoAsenta = false;
	Boolean flagZona = false;
	Boolean flagListSett = false;
	
	boolean inData = true;
	List<String> columns = Arrays.asList("d_codigo", "d_ciudad", "d_estado", "d_asenta", "d_zona", "d_tipo_asenta",
			"D_mnpio");
	Map<String, Integer> mapColumnsNames = new HashMap<>();
	int rowColumnsNames = 0;
	InputStream fileExcel = null;
	String excel;
	
	
	
    /**
     * Method for read Excel file that containst all data about Zip Codes in Mexico
     * @param zipcode Zip Code in Mexico
     */
	public ZipCodeResponse readExcel(String zipCode) {
		
		
		
		Resource resource = new ClassPathResource("/CPdescarga.xls");
		
			try {
				fileExcel = resource.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}


		HSSFWorkbook workbook = null;
		
		try {
			workbook = new HSSFWorkbook(fileExcel);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		workbook.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);

		List<String> sheetNames = new ArrayList<String>();

		for (int i = 1; i < workbook.getNumberOfSheets(); i++) {
			sheetNames.add(workbook.getSheetName(i));
		}


		Sheet sheet = workbook.getSheetAt(1);
		Row row = sheet.getRow(rowColumnsNames);

		row.cellIterator().forEachRemaining(cell -> {

			String valorCelda = cell.getStringCellValue().trim();
			if (!valorCelda.isEmpty()) {
				mapColumnsNames.put(valorCelda, cell.getColumnIndex());
			}
		});
		
		return findZipCodeExcel(sheetNames, sheet, workbook, row, zipCode);
	}	
		

    /**
     * Excel processing for zip code search
     * @param sheetNames List of all sheets of Excel (all States)
     * @param sheet Sheet of Excel
     * @param workbook Workook to work with Excel
     * @param row Row in Excel
     * @param zipCode Zip Code for search
     */
		public ZipCodeResponse findZipCodeExcel(List<String> sheetNames, Sheet sheet, HSSFWorkbook workbook, Row row, String zipCode ) { 	

			List<SettlementsResponse> listSettlements = new ArrayList<SettlementsResponse>();
			SettlementsResponse settlements = new SettlementsResponse();	
			ZipCodeResponse response = new ZipCodeResponse();

			
		for (int state = 1; state <= sheetNames.size(); state++) {

			sheet = workbook.getSheetAt(state);
			Iterator<Row> rowIterator = sheet.iterator();
			inData = true;
			indiceDatos = 0;

			while (rowIterator.hasNext() && inData) {

				filaDatos = sheet.getRow(indiceDatos++);

				row = rowIterator.next();

				if (filaDatos.getCell(1) == null || filaDatos.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK
						|| (filaDatos.getCell(1).getCellType() == Cell.CELL_TYPE_STRING
								&& filaDatos.getCell(1).getStringCellValue().trim().isEmpty())) {
					inData = false;

				} else {
					flag = false;
					flagAsenta = false;
					flagTipoAsenta = false;
					flagZona = false;
					flagListSett = false;
					
					for (String col : columns) {

						celda = filaDatos.getCell(mapColumnsNames.get(col));

						if (col == "d_codigo") {
							if (filaDatos.getCell(mapColumnsNames.get(col)).toString().equals(zipCode)) {
								response.setZip_code(filaDatos.getCell(mapColumnsNames.get(col)).toString());
								flag = true;
								settlements = new SettlementsResponse();
							}
						}

						if (flag) {
							if (col == "d_ciudad") {
								if (celda != null) {
									response.setLocality(celda.toString());
								} else {
									response.setLocality("");
								}

							} else if (col == "d_estado") {
								if (celda != null) {
									response.setFederal_entity(celda.toString());
								} else {
									response.setFederal_entity("");
								}

							} else if (col == "D_mnpio") {
								if (celda != null) {
									response.setMunicipality(celda.toString());
								} else {
									response.setMunicipality("");
								}
							}

							if (col == "d_asenta" || col == "d_zona" || col == "d_tipo_asenta") {
								
								if (col == "d_asenta") {
									if (celda != null) {
										settlements.setName(celda.toString());
									} else {
										settlements.setName("");
									}
									flagAsenta = true;
								} else if (col == "d_zona") {
									if (celda != null) {
										settlements.setZone_type(celda.toString());
									} else {
										settlements.setZone_type("");
									}
									flagZona = true;
								} else if (col == "d_tipo_asenta") {
									if (celda != null) {
										settlements.setSettlement_type(celda.toString());
									} else {
										settlements.setSettlement_type("");
									}
									flagTipoAsenta = true;
								}

								if (flagZona && flagTipoAsenta && flagAsenta) {
									listSettlements.add(settlements);
									flagListSett = true;

								}
							}

						}
						//System.out.print(celda + " ");
					}

					//System.out.println();
				}
				if (flagListSett) {
					response.setSettlements(listSettlements);
					flagListSett = false;
				}

			}
			
			
		}

		return response;

	}

}
