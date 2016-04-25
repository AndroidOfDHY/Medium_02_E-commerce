package com.tuangou.bean;

import java.io.Serializable;
import java.util.List;

public class Meituan implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String url;
	
	private String deal_id;
	private String website;
	private String city_name;
	private String deal_title;
	private String deal_img;
	private String deal_desc;
	private String price;
	private String value;
	private String rebate;
	
	private String sales_num;
	private long start_time;
	private long end_time;
	private String deal_tips;    //新增
	
	private List<Shop> shops;    //新增
	
	/**构造*/
	public Meituan(){
		
	}
	
	public Meituan(int id, String url, String deal_id, String website,
			String city_name, String deal_title, String deal_img,
			String deal_desc, String price, String value, String rebate,
			String sales_num, long start_time, long end_time, String deal_tips,
			List<Shop> shops) {
		super();
		this.id = id;
		this.url = url;
		this.deal_id = deal_id;
		this.website = website;
		this.city_name = city_name;
		this.deal_title = deal_title;
		this.deal_img = deal_img;
		this.deal_desc = deal_desc;
		this.price = price;
		this.value = value;
		this.rebate = rebate;
		this.sales_num = sales_num;
		this.start_time = start_time;
		this.end_time = end_time;
		this.deal_tips = deal_tips;
		this.shops = shops;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDeal_id() {
		return deal_id;
	}
	public void setDeal_id(String deal_id) {
		this.deal_id = deal_id;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getDeal_title() {
		return deal_title;
	}
	public void setDeal_title(String deal_title) {
		this.deal_title = deal_title;
	}
	public String getDeal_img() {
		return deal_img;
	}
	public void setDeal_img(String deal_img) {
		this.deal_img = deal_img;
	}
	public String getDeal_desc() {
		return deal_desc;
	}
	public void setDeal_desc(String deal_desc) {
		this.deal_desc = deal_desc;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRebate() {
		return rebate;
	}
	public void setRebate(String rebate) {
		this.rebate = rebate;
	}
	public String getSales_num() {
		return sales_num;
	}
	public void setSales_num(String sales_num) {
		this.sales_num = sales_num;
	}
	public long getStart_time() {
		return start_time;
	}
	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}
	public long getEnd_time() {
		return end_time;
	}
	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	public List<Shop> getShops() {
		return shops;
	}

	public void setShops(List<Shop> shops) {
		this.shops = shops;
	}

	public String getDeal_tips() {
		return deal_tips;
	}

	public void setDeal_tips(String deal_tips) {
		this.deal_tips = deal_tips;
	}

	@Override
	public String toString() {
		return "Meituan [id=" + id + ", url=" + url + ", deal_id=" + deal_id
				+ ", website=" + website + ", city_name=" + city_name
				+ ", deal_title=" + deal_title + ", deal_img=" + deal_img
				+ ", deal_desc=" + deal_desc + ", price=" + price + ", value="
				+ value + ", rebate=" + rebate + ", sales_num=" + sales_num
				+ ", start_time=" + start_time + ", end_time=" + end_time
				+ ", deal_tips=" + deal_tips + ", shops=" + shops + "]";
	}
	
	
}
