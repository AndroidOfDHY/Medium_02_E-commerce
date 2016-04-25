package com.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.tuangou.bean.Meituan;
import com.tuangou.db.DataIUDS;

public class MyCollectActivity extends MenuActivity 
	implements OnClickListener, OnItemClickListener, OnItemLongClickListener{
	
	private final static String TAG = "mycollectActivity";
	private ListView listView;
	private Button back_bt, back_main_bt, delete_bt;
	private boolean noData;
	private SimpleAdapter adapter;
	private List<Meituan> meituans;
	private DataIUDS data;
	private Meituan mei;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.mycollectactivity);
		
		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		setInListView();
		registerForContextMenu(listView);
		
		back_bt = (Button) findViewById(R.id.back);
		back_bt.setOnClickListener(this);
		
		back_main_bt = (Button) findViewById(R.id.back_main);
		back_main_bt.setOnClickListener(this);
		
		delete_bt = (Button)findViewById(R.id.delete);
		delete_bt.setOnClickListener(this);
	}
	
	/**
	 * 从数据库取出数据，放入listView的adapter中,20个
	 */
	
	private void setInListView(){
		data = new DataIUDS(this);
		
		meituans = new ArrayList<Meituan>();
		//只有20条记录
		meituans = data.getListData(0, 20);
		
		noData = meituans.isEmpty();
		
		if(noData){
			Toast.makeText(this, "您暂时还没有收藏，请重新添加", 1).show();
		}else{
			List<Map<String, String>> lists = new ArrayList<Map<String,String>>();
			for(Meituan mei:meituans){
				Map<String,String> map = new HashMap<String, String>();
				map.put("Website", "【"+mei.getWebsite()+"】");
				map.put("Deal_title", mei.getDeal_title());
				map.put("Price", "￥" +mei.getPrice());
				map.put("Value", "原价:￥" +mei.getValue());
				map.put("Rebate", "折扣:￥" +mei.getRebate());
				lists.add(map);
			}
			
			adapter = new SimpleAdapter(MyCollectActivity.this,lists,R.layout.item,
					new String[] { "Website", "Deal_title", "Price", "Value",
					"Rebate" }, new int[] { R.id.website, R.id.deal_title,
					R.id.price, R.id.value, R.id.rebate });
			
			listView.setAdapter(adapter);
		}
	}
	
	/**
	 * ListView的点击事件
	 */
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		showItemTuan(position);
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		String[] string  = {"打开","删除当前","删除所有"};
		mei = meituans.get(position);
		
		AlertDialog.Builder dialog = new Builder(this);
		dialog.setTitle("请选择").setItems(string, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which == 0){
					showItemTuan(position);
				}else if(which == 1){
					new Builder(MyCollectActivity.this)
					.setTitle("数据删除")
					.setMessage("您确定要删除当前数据吗?")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							data.delete(mei.getId());
							finish();
							Intent intent = new Intent();
							intent.setAction("com.activity.MyCollectActivity");
							startActivity(intent);
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
				}else if(which == 2){
					deleteAllDate();
				}
			}
		}).show();
		return true;
	}

	@Override
	public void onClick(View v) {
		
		if(v == back_bt){	//返回按钮
			this.finish();
		}else if(v == back_main_bt){	//返回主页按钮
			Intent intent =  new Intent();
			intent.setClass(this, MainActivity.class);
			startActivity(intent);
			this.finish();
		}else if(v == delete_bt){
			deleteAllDate();
		}
	}

	/**
	 * 打开详细信息
	 */
    private void showItemTuan(int position){
    	// 取得当前点的数据
    			Meituan mei = meituans.get(position);

    			// 通赤bundle将mei的序列化数据给intent;
    			Bundle bundle = new Bundle();
    			bundle.putSerializable("mei", mei);

    			Intent intent = new Intent();
    			intent.putExtras(bundle);

    			intent.setAction("com.activity.MainActivity01"); // 通过隐式intent来打开shwodescActivity
    			// intent.setClass(tuangouActivity.this,
    			// showdescActivity.class);
    			startActivity(intent);
    }
	
	/**
	 * 删除所有数据	
	 */
	private void deleteAllDate(){
		AlertDialog.Builder diaLog = new Builder(this);
		diaLog.setTitle("数据删除").setMessage("您确定要删除全部数据吗？");
		diaLog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				data.deleteAll();
				Toast.makeText(MyCollectActivity.this, "数据已全部删除", 1).show();
				finish();
				Intent intent = new Intent();
				intent.setAction("com.activity.MyCollectActivity");
				startActivity(intent);
			}
		});
		
		diaLog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		diaLog.show();
	}
}
