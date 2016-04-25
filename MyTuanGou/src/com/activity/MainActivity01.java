package com.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.tuangou.bean.Meituan;
import com.tuangou.bean.Shop;
import com.tuangou.db.DataIUDS;
import com.tuangou.handler.TuangouHandler;

public class MainActivity01 extends MenuActivity implements OnClickListener,OnItemClickListener,OnItemLongClickListener{
	private static final String TAG = "MainActivity01";
	private TextView  website, deal_desc, price, value,rebate,save_price,
				sales_num,start_time,end_time,last_time,shopsList_text,deal_tips_text,deal_tips,share_deal;
	private Button back_bt,collect_bt;
	private ImageView image;
	private Meituan mei;
	private ListView shopsView;
	private List<Shop> shopsList;
	private Button buy,fav;
	private SimpleAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.mainactivity01);
		
		//查找views
		findViews();
		
		//通过反序列化得到Meituan对象
		Bundle bundle = getIntent().getExtras();
		mei = (Meituan)bundle.getSerializable("mei");
		System.out.println(mei.getUrl());
		//团购种类	
		
		website.setText("【"+mei.getWebsite()+"】");
		
		deal_desc.setText(mei.getDeal_desc());
		
		ImgTask imgTask = new ImgTask(MainActivity01.this, mei.getDeal_img(), image);
		imgTask.execute();
		//image.setImageBitmap(TuangouHandler.getBitmap(mei.getDeal_img()));
		
		start_time.setText(TuangouHandler.formatTime(mei.getStart_time()));
		
		end_time.setText(TuangouHandler.formatTime(mei.getEnd_time()));
		
		last_time.setText(TuangouHandler.getLastTime(System.currentTimeMillis()/1000, mei.getEnd_time()));
		
		price.setText("￥" + mei.getPrice());

		value.setText("￥" + mei.getValue());
		
		rebate.setText(mei.getRebate());
		
		if(null==mei.getDeal_tips()){
			deal_tips_text.setVisibility(View.GONE);
			deal_tips.setVisibility(View.GONE);
		}else{
			deal_tips.setText(mei.getDeal_tips());
		}
		
		String s ;
		try{
			double	d = Double.parseDouble(mei.getValue())-Double.parseDouble(mei.getPrice());
			s= String.valueOf(d);
			if(s.indexOf(".")!=-1){
				DecimalFormat myFormatter = new DecimalFormat("####.#");  
				s = myFormatter.format(d);   //注意,这句整体作为String
			}
		}catch(Exception e){
			e.printStackTrace();
			s ="0";
		}
		
		save_price.setText(s);
		
		sales_num.setText("已经有" + mei.getSales_num() + "人购买");
		
	    /**
	     * 将数据绑定到ListView上
	     */
		shopsList = mei.getShops();
		
		if(null==shopsList||shopsList.isEmpty()){
			shopsList_text.setVisibility(View.GONE);
			shopsView.setVisibility(View.GONE);
			System.out.println("shop没有");
		}else{
			for(Shop shop: shopsList){
				System.out.println(shop);
			}
			setInListView();
			shopsView.setAdapter(adapter);
			shopsView.setOnItemClickListener(this);
			shopsView.setOnItemLongClickListener(this);
		}
		
		
//		shop_addr.setText("地址："+addr);
//		
//		shop_name.setText("商家名称: " + name);
//		
//		shop_tel.setText("商家电话: " + tel);
	
		
		//加入收藏夹监听
		fav.setOnClickListener(this);
		//网上购买监听
		buy.setOnClickListener(this);
		//后退监听
		back_bt.setOnClickListener( this);
		//我的收藏监听
		collect_bt.setOnClickListener(this);
		
		share_deal.setOnClickListener(this);
		
	}
	
	/**
	 *  查找相应view 
	 */
	private void findViews(){
		website = (TextView)findViewById(R.id.website);
		//团购描述
		deal_desc = (TextView)findViewById(R.id.deal_desc);
		//图像处理
		image = (ImageView)findViewById(R.id.image);
		//时间处理
		start_time = (TextView)findViewById(R.id.start_time);
		end_time = (TextView) findViewById(R.id.end_time);
		last_time = (TextView)findViewById(R.id.last_time);
		
		//价钱
		price = (TextView)findViewById(R.id.price);
		value = (TextView) findViewById(R.id.value);
		rebate = (TextView) findViewById(R.id.rebate);
		save_price = (TextView) findViewById(R.id.save_price);
		//购买人数
		sales_num = (TextView) findViewById(R.id.sales_num);
		//商店信息
		shopsView = (ListView)findViewById(R.id.shopsList);
		
		shopsList_text = (TextView)findViewById(R.id.shopsList_text);
		deal_tips_text = (TextView)findViewById(R.id.deal_tips_text);
		deal_tips = (TextView)findViewById(R.id.deal_tips);
		
		//加入收藏
		fav = (Button) findViewById(R.id.fav);
		//网上购买
		back_bt = (Button) findViewById(R.id.back);
		//后退
		collect_bt = (Button) findViewById(R.id.collect);
		//我的收藏
		buy = (Button) findViewById(R.id.buy);
		//分享团购信息
		share_deal = (Button) findViewById(R.id.share_deal);
	}
	

	
	@Override
	public void onClick(View v) {
		if(v == fav){
			try{
				DataIUDS data = new DataIUDS(MainActivity01.this);
				data.save(mei); // 数据保存到数据库
				Toast.makeText(MainActivity01.this, "收藏添加成功", 1).show();
			}catch(Exception e){
				Log.e(TAG,e.toString());
				Toast.makeText(MainActivity01.this, "添加收藏失败，请重新添加", 1).show();
			}
		}else if(v == back_bt){
			this.finish();
		}else if(v == collect_bt){
			Intent intent = new Intent();
			intent.setAction("com.activity.MyCollectActivity");
			startActivity(intent);
		}else if(v == buy){
			 String url = mei.getUrl();   
			 Intent intent = new Intent(Intent.ACTION_VIEW);   
			 intent.setData(Uri.parse(url));   
			 startActivity(intent);  
		}else if(v == share_deal){
			String[] strings = {"短信分享","邮件分享"};
			AlertDialog.Builder builder = new Builder(MainActivity01.this);
			builder.setTitle("团购分享").setItems(strings, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String str = formatSMS();
					if(which==0){
//						SmsManager smsManager = SmsManager.getDefault();
//						smsManager.sendTextMessage(null, null, str, null, null);
						Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
						 intent.putExtra("sms_body", str);
						 startActivity(intent);
						 
					}else if(which == 1){
						try
						{
							final Intent emailIntent = new Intent(
									android.content.Intent.ACTION_SEND);
							emailIntent.setType("plain/text");
							emailIntent.putExtra(
									android.content.Intent.EXTRA_SUBJECT,
									"团购信息");
							emailIntent.putExtra(
									android.content.Intent.EXTRA_TEXT,str
									);
							startActivity(Intent.createChooser(emailIntent,
									null));
						} catch (Exception e)
						{
							Toast.makeText(MainActivity01.this,
									"请修改您手机里的邮件设置", 1).show();
						}
					}
				}
			}).show();
			
		}
	}
	
	
    class ImgTask extends AsyncTask<String, Integer, String> {
        // 可变长的输入参数，与AsyncTask.exucute()对应
        ImageView img ;
        String url;
        Context con;
        Bitmap bm;
        
        public ImgTask(Context c,String url,ImageView img){
        	this.con = c;
        	this.url = url;
        	this.img = img;
        }
        
		@Override
		protected String doInBackground(String... params) {
			bm = TuangouHandler.getBitmap(url);
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			img.setImageBitmap(bm);
			super.onPostExecute(result);
		}
     }
    
	private void setInListView() {
		List<Map<String,String>> lists = new ArrayList<Map<String,String>>();
		for(Shop shop:shopsList){
			Map<String,String> map = new HashMap<String, String>();
			map.put("Shop_name", "【"+shop.getShop_name()+"】");
			map.put("Shop_addr", "地址:"+shop.getShop_addr());
			map.put("Shop_tel", "电话:"+shop.getShop_tel());
			lists.add(map);
			
		}
		
		adapter = new SimpleAdapter(MainActivity01.this,lists,R.layout.shopitem,
				new String[] { "Shop_name", "Shop_addr","Shop_tel"}, 
				new int[] { R.id.shop_name, R.id.shop_addr,R.id.shop_tel});
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		String mobile = shopsList.get(position).getShop_tel();
		if(mobile.indexOf("/")!=-1){
			mobile = mobile.substring(0, mobile.indexOf("/"));
		}
		Intent intent = new Intent(Intent.ACTION_CALL ,Uri.parse("tel:"+mobile));
		MainActivity01.this.startActivity(intent);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Shop shop = shopsList.get(position);
		if (shop.getShop_long() == null || shop.getShop_lat() == null
				|| shop.getShop_long().equals("")|| shop.getShop_lat().equals(""))
		{
			Toast.makeText(this, getString(R.string.nomapinfo), 1).show();
		} else
		{
			// 店铺的经度和纬度
			String shop_long = shop.getShop_long();
			String shop_lat = shop.getShop_lat();

			System.out.println(shop_long);
			System.out.println(shop_lat);

			Intent intent = new Intent();
			intent.putExtra("shop_name",shop.getShop_name());
			intent.putExtra("shop_tel", shop.getShop_tel());
			intent.putExtra("shop_addr", shop.getShop_addr());
			intent.putExtra("shop_long", shop_long);
			intent.putExtra("shop_lat", shop_lat);
			intent.setClass(MainActivity01.this, MyMapActivity.class);
			//intent.setAction("com.activity.MyMapActivity");
			startActivity(intent);
		}
		
	}
	
	private String formatSMS(){
		String str = mei.getDeal_desc()+"\n网址："+mei.getUrl();
		return str;
		
	}
}
