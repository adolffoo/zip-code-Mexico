package com.gendra.zipCode.models.response;

/**
 * Model SettlementsResponse Class
 * @author Adolfo Marín
 * @version 1.0
 */

public class SettlementsResponse {

	String name;
	String zone_type;
	String settlement_type;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZone_type() {
		return zone_type;
	}
	public void setZone_type(String zone_type) {
		this.zone_type = zone_type;
	}
	public String getSettlement_type() {
		return settlement_type;
	}
	public void setSettlement_type(String settlement_type) {
		this.settlement_type = settlement_type;
	}
	
	
}
