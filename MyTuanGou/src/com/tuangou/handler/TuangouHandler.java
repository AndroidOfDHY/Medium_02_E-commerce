package com.tuangou.handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Xml;

import com.tuangou.bean.Meituan;
import com.tuangou.bean.Shop;

public class TuangouHandler {
	public final static int MEITUAN = 0;
	public final static int LASHOU = 1;
	public final static int FTUAN = 2;
	public final static int NUOMI = 3;
	
	/**
	 * 解析方式为pull解析器
	 * 
	 * @param input  团购网站输入流
	 * @param website 团购网站的名称
	 * 
	 * @return  团购数据列列表
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 * 
	 * @throws Exception
	 */
	
	public static List<Meituan> getListMeituan(InputStream input,int web) throws XmlPullParserException, IOException {
		String data = null;
		String website = null;
		String city_name = null;
		String deal_id = null;
		String deal_title = null;
		String deal_url = null;
		String deal_img = null;
		String deal_desc = null;
		String sales_num = null;
		String value = null;
		String price = null;
		String rebate = null;
		String start_time = null;
		String end_time = null;
		String deal_tips = null;   //新增
		
		//商店
		String shop_name = null;
		String shop_tel = null;
		String shop_addr = null;
		String shop_long = null;
		String shop_lat = null;
		String shops_s = null;
		String shop_s = null;
		
		boolean isLashou = false;
		
		List<Meituan> meituans = null;
		Meituan mei = null;
		List<Shop> shops = null;
		Shop shop = null;
		
		switch(web){
		case MEITUAN:
			data = "data";
			deal_url = "deal_url";
			website = "website";
			deal_id = "deal_id";
			city_name = "city_name";
			deal_title = "deal_title";
			deal_img = "deal_img";
			deal_desc = "deal_desc";
			sales_num = "sales_num";
			value = "value";
			price = "price";
			rebate = "rebate";
			start_time = "start_time";
			end_time = "end_time";
			deal_tips = "deal_tips";
			
			shop_name = "shop_name";
			shop_tel = "shop_tel";
			shop_addr = "shop_addr";
			shop_long = "shop_long";
			shop_lat = "shop_lat";
			shops_s = "shops";
			shop_s = "shop";
			break;
			
			
		case LASHOU:
		case FTUAN:
		case NUOMI:
			data = "url";
			deal_url = "loc";
			website = "website";
			deal_id = "gid";
			city_name = "city";
			deal_title = "title";
			deal_img = "image";
			deal_desc = "deal_desc";
			sales_num = "bought";
			value = "value";
			price = "price";
			rebate = "rebate";
			start_time = "startTime";
			end_time = "endTime";
			deal_tips = "deal_tips";		//新增
			
			shop_name = "name";
			shop_tel = "tel";
			shop_addr = "addr";
			shop_long = "latitude";
			shop_lat = "longitude";
			shops_s = "shops";
			shop_s = "shop";
			
			isLashou = true;

			break;
		}
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(input, "UTF-8");
		int eventType = parser.getEventType(); // 产生第一个事件

		while (eventType != XmlPullParser.END_DOCUMENT) { // 只要不是文档结束事件
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				meituans = new ArrayList<Meituan>();
				break;
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				if (data.equals(name)) {
					mei = new Meituan();
				}

				if (null != mei) {
					if (website.equals(name)) {
						mei.setWebsite(parser.nextText());
					}
					if (city_name.equals(name)) {
						mei.setCity_name(parser.nextText());
					}
					if (deal_id.equals(name)) {
						mei.setDeal_id(parser.nextText());
					}
					if (deal_title.equals(name)) {
						mei.setDeal_title(parser.nextText());
					}
					if (deal_url.equals(name)) {
						mei.setUrl(parser.nextText());
					}
					if (deal_img.equals(name)) {
						mei.setDeal_img(parser.nextText());
					}
					if (deal_desc.equals(name)) {
						mei.setDeal_desc(parser.nextText());
					}
					if (value.equals(name)) {
						mei.setValue(parser.nextText());
					}
					if (price.equals(name)) {
						mei.setPrice(parser.nextText());
					}
					if (rebate.equals(name)) {
						mei.setRebate(parser.nextText());
					}
					if (sales_num.equals(name)) {
						mei.setSales_num(parser.nextText());
					}
					if (start_time.equals(name)) {
						mei.setStart_time(new Long(parser.nextText()));
					}
					if (end_time.equals(name)) {
						mei.setEnd_time(new Long(parser.nextText()));
					}
					if (deal_tips.equals(name)) {
						mei.setDeal_tips(parser.nextText());
					}
				}
				
				if(shops_s.equals(name)){
					shops = new ArrayList<Shop>();
				}
				if(shop_s.equals(name)){
					shop = new Shop();
				}
				if(null != shop){
					if (shop_name.equals(name)) {
						shop.setShop_name(parser.nextText());
					}
					if (shop_tel.equals(name)) {
						shop.setShop_tel(parser.nextText());
					}
					if (shop_addr.equals(name)) {
						shop.setShop_addr(parser.nextText());
					}
					if (shop_long.equals(name)) {
						shop.setShop_long(parser.nextText());
					}
					if (shop_lat.equals(name)) {
						shop.setShop_lat(parser.nextText());
					}
				}
				break;
				
			case XmlPullParser.END_TAG:
				String str = parser.getName();
				if(shop_s.equals(str)){
					if(null!=shops){
						shops.add(shop);
					}
					shop = null;
				}
				if(shops_s.equals(str)){
					if(null!=mei){
						mei.setShops(shops);
					}
					shops = null;
				}
				if(data.equals(str)){
					//判断是否是拉手网
					if(isLashou){
						String str1 = mei.getDeal_title();
						mei.setDeal_desc(str1);
						mei.setDeal_title(str1.substring(0,30)+"...");
					}
					meituans.add(mei);
					mei = null;
				}
				
				break;
			}
			eventType = parser.next();
		}
		input.close();
		
		return meituans;
	}
	
	/**
	 * 从网络地址取得图片
	 * 
	 * @param path
	 *            网络地址
	 * @return
	 */

	public static Bitmap getBitmap(String path)
	{
		try
		{
			URL url = new URL(path);
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();

			InputStream input = httpConn.getInputStream();
			Bitmap image = BitmapFactory.decodeStream(input);

			return image;

		} catch (Exception e)
		{
			Log.i("getBitmap", e.toString());
		}
		return null;
	}
	
	/**
	 * 时间格式化
	 * 
	 * @return  yyyy-MM-dd
	 */
	public static String formatTime(long start_time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = new Date(start_time * 1000);
		String data = sdf.format(date);
		return data;
	}
	
	/**
	 * 计算剩余团购时间
	 * @param start_time
	 * @param end_time
	 * @return str
	 */
	public static String getLastTime(long start_time,long end_time){
		long last_time = end_time - start_time;
		long day = last_time / (24 * 60 * 60);
		long hour = (last_time / (60 * 60) - day * 24);

		String str = "还有" + day + "天" + hour + "小时";
		return str;
	}
}
