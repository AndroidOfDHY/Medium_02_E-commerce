package com.tuangou.bean;

import java.io.Serializable;

public class Shop implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String shop_name;
	private String shop_addr;
	private String shop_tel;
	private String shop_long;
	private String shop_lat;
	
	public Shop(){
		
	}
	
	public Shop(int id, String shop_name, String shop_addr, String shop_tel,
			String shop_long, String shop_lat) {
		super();
		this.id = id;
		this.shop_name = shop_name;
		this.shop_addr = shop_addr;
		this.shop_tel = shop_tel;
		this.shop_long = shop_long;
		this.shop_lat = shop_lat;
	}



	public String getShop_name() {
		return shop_name;
	}
	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}
	public String getShop_addr() {
		return shop_addr;
	}
	public void setShop_addr(String shop_addr) {
		this.shop_addr = shop_addr;
	}
	public String getShop_tel() {
		return shop_tel;
	}
	public void setShop_tel(String shop_tel) {
		this.shop_tel = shop_tel;
	}
	public String getShop_long() {
		return shop_long;
	}
	public void setShop_long(String shop_long) {
		this.shop_long = shop_long;
	}
	public String getShop_lat() {
		return shop_lat;
	}
	public void setShop_lat(String shop_lat) {
		this.shop_lat = shop_lat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Shop [id=" + id + ", shop_name=" + shop_name + ", shop_addr="
				+ shop_addr + ", shop_tel=" + shop_tel + ", shop_long="
				+ shop_long + ", shop_lat=" + shop_lat + "]";
	}
	
	
}
