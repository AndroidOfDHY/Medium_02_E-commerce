package com.tuangou.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TuangouData extends SQLiteOpenHelper{
	private final static String DATA_BASE = "MyTuangouCollect.db";	//数据库名称
	private final static int DATA_VERSION = 1;		//数据库版本号
	
	public TuangouData(Context context){
		super(context, DATA_BASE, null, DATA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table tuan_collect " +
				"(_id integer primary key autoincrement," +
				"url text, website varchar(20), deal_id varchar(20), " +
				"city_name text , deal_title text , deal_img text , " +
				"deal_desc text , price varchar(20), value varchar(20) , " +
				"rebate varchar(20) , sales_num varchar(20) , start_time integer , " +
				"end_time integer , shop_name varchar(100), shop_addr varchar(100)," +
				" shop_area varchar(100),shop_tel varchar(30),deal_tips text)";
		db.execSQL(sql);
		sql = "create table tuan_shop " +
				"(_id integer primary key autoincrement," +
				"shop_name varchar(20),shop_addr text," +
				"shop_tel varchar(20),shop_long varchar(20)," +
				"shop_lat varchar(20),deal_id varchar(20))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//更新版本，可使用本项
		db.execSQL("drop table tuan_collect");
		onCreate(db);
	}
	
}
