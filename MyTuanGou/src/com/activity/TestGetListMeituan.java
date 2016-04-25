package com.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

import com.tuangou.bean.Meituan;
import com.tuangou.db.DataIUDS;
import com.tuangou.db.TuangouData;
import com.tuangou.handler.TuangouHandler;


public class TestGetListMeituan extends AndroidTestCase
{
	public void testGetList() throws Exception{
		
		String path = "http://www.meituan.com/api/v2/beijing/deals";
		URL url = new URL(path);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

		int responseCode = httpConn.getResponseCode();

		if (responseCode == 200)
		{
			InputStream input = httpConn.getInputStream();
			
//			XMLHandler handler = new XMLHandler();
			
			List<Meituan> meituans = TuangouHandler.getListMeituan(input, TuangouHandler.MEITUAN);
			
//			DataIUDS data = new DataIUDS(this.getContext());
//
			for(Meituan mei : meituans){
				System.out.println(mei);
			}
		}
		
	}
	
	public void testDatabase()throws Exception{
		TuangouData data = new TuangouData(this.getContext());
		data.getWritableDatabase();
	}
	
	public void testfind() throws Exception{
		DataIUDS data = new DataIUDS(this.getContext());
		Meituan mei = data.find(2);
		
		System.out.println(mei);
		
	}
	
	public void testupdate() throws Exception{
		DataIUDS data = new DataIUDS(this.getContext());
		Meituan mei = new Meituan();
		mei.setCity_name("ÎÞÎý");
		data.update(mei, 1);
		Meituan mei1 =data.find(1);
		System.out.println(mei1);
	}
	
	public void testdelete() throws Exception{
		DataIUDS data = new DataIUDS(this.getContext());
		data.delete(1);
		
		Meituan mei = data.find(1);
		System.out.println(mei);
		
		
	}
	
	public void testgetListMeituan() throws Exception{
		boolean noData = false;
		DataIUDS data = new DataIUDS(this.getContext());
		
		List<Meituan> meituans = new ArrayList<Meituan>();
		meituans = data.getListData(0, 10);
		noData = meituans.isEmpty() ? true : false;
		
		for(Meituan mei : meituans){
			
			System.out.println(mei);
		}
		
		System.out.println(noData);
		

	}
	
	public void testgetCount() throws Exception{
		
		DataIUDS data = new DataIUDS(this.getContext());
		int count = data.getCount();
		System.out.println(count);
		
		
	}
	
//	public void testDataIUDSSave() throws Exception{
//		
//		meituan mei = new meituan();
//		mei.setWebsite("");
//		mei.setCity_name("");
//		mei.setd
//		
//		data.save(mei);
//		
//		
//	}
}
