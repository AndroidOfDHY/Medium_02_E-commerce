package com.tuangou.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tuangou.bean.Meituan;
import com.tuangou.bean.Shop;

public class DataIUDS {
	private TuangouData data;
	
	public DataIUDS(Context context){
		this.data = new TuangouData(context);
	}
	
	public void save(Meituan mei) throws Exception{
		SQLiteDatabase db = data.getWritableDatabase();
		try{
			db.execSQL("delete from tuan_collect where deal_id=?",new String[]{String.valueOf(mei.getDeal_id())});
		}catch(SQLException e){
			e.printStackTrace();
		}
		db.execSQL("insert into tuan_collect ( url, website, deal_id, city_name, deal_title, deal_img, deal_desc, price, value, rebate, sales_num, start_time, end_time) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[] {mei.getUrl(), mei.getWebsite(), mei.getDeal_id(),
						mei.getCity_name(), mei.getDeal_title(),
						mei.getDeal_img(), mei.getDeal_desc(), mei.getPrice(),
						mei.getValue(), mei.getRebate(), mei.getSales_num(),
						mei.getStart_time(), mei.getEnd_time()
						});
		if(null!=mei.getShops()){
			for(Shop shop :mei.getShops()){
				//shop_name ,shop_addr,shop_tel ,shop_long,shop_lat,deal_id
				db.execSQL("insert into tuan_shop(shop_name,shop_addr,shop_tel,shop_long,shop_lat,deal_id) values (?,?,?,?,?,?)",
						new Object[]{shop.getShop_name(),shop.getShop_addr(),
						shop.getShop_tel(),shop.getShop_long(),shop.getShop_lat(),mei.getDeal_id()});
			}
		}
		db.close();
	}
	
	public void delete(int id){
		SQLiteDatabase db = data.getWritableDatabase();
		db.execSQL("delete from tuan_collect where _id = ?",new String[]{String.valueOf(id)});
		db.close();
	}
	
	public void deleteAll(){
		SQLiteDatabase db = data.getWritableDatabase();
		db.execSQL("delete from tuan_shop");
		db.execSQL("delete from tuan_collect");
		db.close();
	}
	
	public Meituan find(int id) throws Exception
	{
		SQLiteDatabase db = data.getReadableDatabase(); // 得到用于数据的数据库实例
		Cursor cursor = db.rawQuery("select * from tuan_collect where _id = ?",
				new String[] { String.valueOf(id) });
		Meituan mei = new Meituan();
		if (cursor.moveToFirst())
		{
			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			String url = cursor.getString(cursor.getColumnIndex("url"));
			String website = cursor.getString(cursor.getColumnIndex("website"));
			String deal_id = cursor.getString(cursor.getColumnIndex("deal_id"));
			String city_name = cursor.getString(cursor.getColumnIndex("city_name"));
			String deal_title = cursor.getString(cursor.getColumnIndex("deal_title"));
			String deal_img = cursor.getString(cursor.getColumnIndex("deal_img"));
			String deal_desc = cursor.getString(cursor.getColumnIndex("deal_desc"));
			String price = cursor.getString(cursor.getColumnIndex("price"));
			String value = cursor.getString(cursor.getColumnIndex("value"));
			String rebate = cursor.getString(cursor.getColumnIndex("rebate"));
			String sales_num = cursor.getString(cursor.getColumnIndex("sales_num"));
			long start_time = cursor.getLong(cursor.getColumnIndex("start_time"));
			long end_time = cursor.getLong(cursor.getColumnIndex("end_time"));
			
			List<Shop> shopList = new ArrayList<Shop>();
			Cursor shopCursor = db.rawQuery("select * from tuan_shop where deal_id = ?", new String[]{deal_id});
			while(shopCursor.moveToNext()){
				Shop shop = new Shop();
				int shop_id = shopCursor.getInt(shopCursor.getColumnIndex("_id"));
				String shop_name = shopCursor.getString(shopCursor.getColumnIndex("shop_name"));
				String shop_addr = shopCursor.getString(shopCursor.getColumnIndex("shop_addr"));
				String shop_tel = shopCursor.getString(shopCursor.getColumnIndex("shop_tel"));
				String shop_long = shopCursor.getString(shopCursor.getColumnIndex("shop_long"));
				String shop_lat = shopCursor.getString(shopCursor.getColumnIndex("shop_lat"));
				shop.setId(shop_id);
				shop.setShop_name(shop_name);
				shop.setShop_addr(shop_addr);
				shop.setShop_tel(shop_tel);
				shop.setShop_long(shop_long);
				shop.setShop_lat(shop_lat);
				
				shopList.add(shop);
			}
			
			mei.setId(_id);
			mei.setUrl(url);
			mei.setWebsite(website);
			mei.setDeal_id(deal_id);
			mei.setCity_name(city_name);
			mei.setDeal_title(deal_title);
			mei.setDeal_img(deal_img);
			mei.setDeal_desc(deal_desc);
			mei.setPrice(price);
			mei.setValue(value);
			mei.setRebate(rebate);
			mei.setSales_num(sales_num);
			mei.setStart_time(start_time);
			mei.setEnd_time(end_time);
			mei.setShops(shopList);
		}
		cursor.close();
		db.close();
		
		return mei;
	}
	
	// 根据ID来更新数据
	public void update(Meituan mei, int id)
	{
		SQLiteDatabase db = data.getWritableDatabase();
		db.execSQL(
				"update tuan_collect set website=?, deal_id=?, city_name=?, deal_title=?, deal_img=?, deal_desc=?, price=?, value=?, rebate =?, sales_num=?, start_time=?, end_time=?, shop_name=?, shop_addr=?, shop_area=?, shop_tel=? where _id=?",
				new Object[] { 
						mei.getWebsite(), mei.getDeal_id(),
						mei.getCity_name(), mei.getDeal_title(),
						mei.getDeal_img(), mei.getDeal_desc(), mei.getPrice(),
						mei.getValue(), mei.getRebate(), mei.getSales_num(),
						mei.getStart_time(), mei.getEnd_time(),
					//	mei.getShop_name(), mei.getShop_addr(),
					//	mei.getShop_area(), mei.getShop_tel(),
						String.valueOf(id) 
						});

		db.close();
	}
	
	public List<Meituan> getListData(int offset, int maxResult)
	{
		SQLiteDatabase db = data.getReadableDatabase();
		List<Meituan> meituans = new ArrayList<Meituan>();
		Cursor cursor = db.rawQuery(
				"select * from tuan_collect order by _id desc limit ? , ?",
				new String[] { String.valueOf(offset),
						String.valueOf(maxResult) });

		while (cursor.moveToNext())
		{
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			String url = cursor.getString(cursor.getColumnIndex("url"));
			String website = cursor.getString(cursor.getColumnIndex("website"));
			String deal_id = cursor.getString(cursor.getColumnIndex("deal_id"));
			String city_name = cursor.getString(cursor.getColumnIndex("city_name"));
			String deal_title = cursor.getString(cursor.getColumnIndex("deal_title"));
			String deal_img = cursor.getString(cursor.getColumnIndex("deal_img"));
			String deal_desc = cursor.getString(cursor.getColumnIndex("deal_desc"));
			String price = cursor.getString(cursor.getColumnIndex("price"));
			String value = cursor.getString(cursor.getColumnIndex("value"));
			String rebate = cursor.getString(cursor.getColumnIndex("rebate"));
			String sales_num = cursor.getString(cursor.getColumnIndex("sales_num"));
			long start_time = cursor.getLong(cursor.getColumnIndex("start_time"));
			long end_time = cursor.getLong(cursor.getColumnIndex("end_time"));

			//shop_name ,shop_addr,shop_tel ,shop_long,shop_lat,deal_id
			List<Shop> shopList = new ArrayList<Shop>();
			try{
				Cursor shopCursor = db.rawQuery("select * from tuan_shop where deal_id = ?", new String[]{deal_id});
				while(shopCursor.moveToNext()){
					Shop shop = new Shop();
					int shop_id = shopCursor.getInt(shopCursor.getColumnIndex("_id"));
					String shop_name = shopCursor.getString(shopCursor.getColumnIndex("shop_name"));
					String shop_addr = shopCursor.getString(shopCursor.getColumnIndex("shop_addr"));
					String shop_tel = shopCursor.getString(shopCursor.getColumnIndex("shop_tel"));
					String shop_long = shopCursor.getString(shopCursor.getColumnIndex("shop_long"));
					String shop_lat = shopCursor.getString(shopCursor.getColumnIndex("shop_lat"));
					shop.setId(shop_id);
					shop.setShop_name(shop_name);
					shop.setShop_addr(shop_addr);
					shop.setShop_tel(shop_tel);
					shop.setShop_long(shop_long);
					shop.setShop_lat(shop_lat);
					
					shopList.add(shop);
				}
			}catch(Exception e){
				e.printStackTrace();
				shopList = null;
			}
				
			
			Meituan mei = new Meituan();
			mei.setId(id);
			mei.setUrl(url);
			mei.setWebsite(website);
			mei.setDeal_id(deal_id);
			mei.setCity_name(city_name);
			mei.setDeal_title(deal_title);
			mei.setDeal_img(deal_img);
			mei.setDeal_desc(deal_desc);
			mei.setPrice(price);
			mei.setValue(value);
			mei.setRebate(rebate);
			mei.setSales_num(sales_num);
			mei.setStart_time(start_time);
			mei.setEnd_time(end_time);
			mei.setShops(shopList);

			meituans.add(mei);
		}

		cursor.close();
		db.close();
		return meituans;
	}

	public int getCount(){
		SQLiteDatabase db = data.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from tuan", null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		db.close();
		return count;
	}
}
