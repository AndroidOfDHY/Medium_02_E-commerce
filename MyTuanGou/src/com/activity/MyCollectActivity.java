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
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
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
	 * �����ݿ�ȡ�����ݣ�����listView��adapter��,20��
	 */
	
	private void setInListView(){
		data = new DataIUDS(this);
		
		meituans = new ArrayList<Meituan>();
		//ֻ��20����¼
		meituans = data.getListData(0, 20);
		
		noData = meituans.isEmpty();
		
		if(noData){
			Toast.makeText(this, "����ʱ��û���ղأ����������", 1).show();
		}else{
			List<Map<String, String>> lists = new ArrayList<Map<String,String>>();
			for(Meituan mei:meituans){
				Map<String,String> map = new HashMap<String, String>();
				map.put("Website", "��"+mei.getWebsite()+"��");
				map.put("Deal_title", mei.getDeal_title());
				map.put("Price", "��" +mei.getPrice());
				map.put("Value", "ԭ��:��" +mei.getValue());
				map.put("Rebate", "�ۿ�:��" +mei.getRebate());
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
	 * ListView�ĵ���¼�
	 */
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		showItemTuan(position);
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		String[] string  = {"��","ɾ����ǰ","ɾ������"};
		mei = meituans.get(position);
		
		AlertDialog.Builder dialog = new Builder(this);
		dialog.setTitle("��ѡ��").setItems(string, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which == 0){
					showItemTuan(position);
				}else if(which == 1){
					new Builder(MyCollectActivity.this)
					.setTitle("����ɾ��")
					.setMessage("��ȷ��Ҫɾ����ǰ������?")
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							data.delete(mei.getId());
							finish();
							Intent intent = new Intent();
							intent.setAction("com.activity.MyCollectActivity");
							startActivity(intent);
						}
					}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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
		
		if(v == back_bt){	//���ذ�ť
			this.finish();
		}else if(v == back_main_bt){	//������ҳ��ť
			Intent intent =  new Intent();
			intent.setClass(this, MainActivity.class);
			startActivity(intent);
			this.finish();
		}else if(v == delete_bt){
			deleteAllDate();
		}
	}

	/**
	 * ����ϸ��Ϣ
	 */
    private void showItemTuan(int position){
    	// ȡ�õ�ǰ�������
    			Meituan mei = meituans.get(position);

    			// ͨ��bundle��mei�����л����ݸ�intent;
    			Bundle bundle = new Bundle();
    			bundle.putSerializable("mei", mei);

    			Intent intent = new Intent();
    			intent.putExtras(bundle);

    			intent.setAction("com.activity.MainActivity01"); // ͨ����ʽintent����shwodescActivity
    			// intent.setClass(tuangouActivity.this,
    			// showdescActivity.class);
    			startActivity(intent);
    }
	
	/**
	 * ɾ����������	
	 */
	private void deleteAllDate(){
		AlertDialog.Builder diaLog = new Builder(this);
		diaLog.setTitle("����ɾ��").setMessage("��ȷ��Ҫɾ��ȫ��������");
		diaLog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				data.deleteAll();
				Toast.makeText(MyCollectActivity.this, "������ȫ��ɾ��", 1).show();
				finish();
				Intent intent = new Intent();
				intent.setAction("com.activity.MyCollectActivity");
				startActivity(intent);
			}
		});
		
		diaLog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		diaLog.show();
	}
}
