package com.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tuangou.bean.Meituan;
import com.tuangou.handler.TuangouHandler;

public class MainActivity extends MenuActivity implements OnClickListener,OnItemClickListener{
	private static final String TAG = "MainActivity";
	private ListView listView;
	private List<Meituan> meituans;
	private Button meituan,lashou,nuomi,ftuan,city_bt;
	private TextView city_text;
	private ProgressDialog dialog;
	private String[] citys_china;
	private String[] citys;
	private SimpleAdapter adapter;
	private int city_position;
	private String city_str;
	private View nowSelect ;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ȥ��������
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mainactivity);
        
        citys = getResources().getStringArray(R.array.citys);
        citys_china = getResources().getStringArray(R.array.citys_china);
        
        readSharedPreaferences();
        city_str = citys_china[city_position];
        city_text = (TextView)findViewById(R.id.city_text);
        
        dialog = new ProgressDialog(this);
        dialog.setTitle("�Ź���Ϣ");
        dialog.setMessage("��������,���Ժ�...");
        
        //���Ű�ť�¼�
        meituan = (Button)findViewById(R.id.meituan);
        meituan.setOnClickListener(this);
        
        //���ְ�ť�¼�
        lashou = (Button)findViewById(R.id.lashou);
        lashou.setOnClickListener(this);
        
        //F�Ű�ť�¼�
        ftuan = (Button)findViewById(R.id.ftuan);
        ftuan.setOnClickListener(this);
        
        //Ŵ���Ű�ť�¼�
        nuomi = (Button)findViewById(R.id.nuomi);
        nuomi.setOnClickListener(this);
        
        //����ѡ��ť
		city_bt = (Button) findViewById(R.id.city);
		city_bt.setOnClickListener(this);
		
		//listView����¼�
		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(this);
		//http://open.client.lashou.com/api/detail/city/beijing/p/1/r/10
        String path = "http://open.client.lashou.com/api/detail/city/"
        		+URLEncoder.encode(citys_china[city_position])+"/p/1/r/10";
        nowSelect = lashou;
        progressShow(path,TuangouHandler.LASHOU);
    }
    
    /**
     * ���н����ϰ�ť����¼�����
     */
  	@Override
  	public void onClick(View v) {
  		// �жϰ�ť����
  		if(v == city_bt){
  			AlertDialog.Builder city_dialog = new AlertDialog.Builder(this);
  			city_dialog.setTitle("��ѡ������Ҫ�鿴�ĳ���");
  			city_dialog.setSingleChoiceItems(citys_china, -1, 
  					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							city_position = which;
							city_str = citys_china[city_position];
							Toast.makeText(MainActivity.this, 
									"���鿴�ĳ���Ϊ"+citys_china[city_position], 1).show();
							createUrl(nowSelect);
							
							dialog.dismiss();
						}
					});
  			city_dialog.show();
  		}else{
  			createUrl(v);
  		}
  	}
    
  	/**
  	 * ���ݰ�ť�¼�������Ӧ����
  	 * @param v
  	 */
  	private void createUrl(View v) {
  		if(v == meituan){
  			//http://www.meituan.com/api/v2/beijing/deals
  			String path = "http://www.meituan.com/api/v2/"
  					+ citys[city_position] + "/deals";
  			nowSelect = meituan;
  			progressShow(path,TuangouHandler.MEITUAN);
  		}else if(v == lashou){
  			//http://open.client.lashou.com/api/detail/city/����/p/1/r/10
  			String path = "http://open.client.lashou.com/api/detail/city/"
					+ URLEncoder.encode(citys_china[city_position])
					+ "/p/1/r/10";
  			nowSelect = lashou;
  			progressShow(path, TuangouHandler.LASHOU);
  		}else if(v == ftuan){
  			//http://newapi.ftuan.com/api/v2.aspx?city=beijing
  			String path = "http://newapi.ftuan.com/api/v2.aspx?city="
					+ citys[city_position];
  			nowSelect = ftuan;
  			progressShow(path, TuangouHandler.FTUAN);
  		}else if(v == nuomi){
  			//http://www.nuomi.com/api/dailydeal?version=v1&city=beijing
  			String path = "http://www.nuomi.com/api/dailydeal?version=v1&city="
					+ citys[city_position];
  			nowSelect = nuomi;
			progressShow(path, TuangouHandler.NUOMI);
  		}
	}
  	
  	/**
  	 * ͨ���߳���������������
  	 * @param path web·��
  	 * @param web	
  	 */
    private void progressShow(final String path,final int web){
    	dialog.show();
    	
    	final Handler myHandler = new Handler(){
    		@Override
    		public void handleMessage(Message msg) {
    			if(msg.what==1){
    				dialog.cancel();	
    				city_text.setText(city_str);
    				listView.setAdapter(adapter);
    			}
    		}
    	};
    	
    	new Thread(){
    			public void run(){
    				//���̵߳�ѭ����־λ
    				Looper.prepare();
    				
//    				try{
//    					Thread.sleep(500);
//    				}catch(InterruptedException e){
//    					e.printStackTrace();
//    				}
    				
    				//������ҳ����
    				urlConn(path,web);
    				
    				//��handle���͵���Ϣ
    				Message m = new Message();
    				m.what = 1;
    				myHandler.sendMessage(m);
    				
    				Looper.loop();
    			};
    	}.start();
    }
    
    /**
     * ������ҳXML����
     * 
     * @param path  ��ҳ��ַ
     * 
     * @param website	��վ���� 0Ϊ���ţ�1Ϊ���֣�2ΪF�ţ�3ΪŴ��
     */
    private void urlConn(String path, int website){
    	try{
    		//HTTP���紫��
    		URL url = new URL(path);
    		HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
    		
    		int responseCode = httpConn.getResponseCode();
    		
    		if(responseCode == 200){
    			InputStream input = httpConn.getInputStream();
    			
    			meituans = TuangouHandler.getListMeituan(input, website);
    			
    			setInListView();	//�����ݼ���listview��adapter��
    			
    			input.close();
    			httpConn.disconnect();
    		}
    	}catch(Exception e){
    		Log.e(TAG,e.toString());
    		Toast.makeText(this, "������·���ӳ�����ȷ�����������Ѵ�", 1).show();
    	}
    }
    
    /**
     * �����ݰ󶨵�ListView��
     */
	private void setInListView() {
		List<Map<String,String>> lists = new ArrayList<Map<String,String>>();
		if(null!=meituans){
			for(Meituan mei:meituans){
				Map<String,String> map = new HashMap<String, String>();
				map.put("Website", "��"+mei.getWebsite()+"��");
				map.put("Deal_title", mei.getDeal_title());
				map.put("Price", "��" +mei.getPrice());
				map.put("Value", "ԭ��:��" +mei.getValue());
				map.put("Rebate", "�ۿ�:��" +mei.getRebate());
				lists.add(map);
			}
		}
		
		adapter = new SimpleAdapter(MainActivity.this,lists,R.layout.item,
				new String[] { "Website", "Deal_title", "Price", "Value",
				"Rebate" }, new int[] { R.id.website, R.id.deal_title,
				R.id.price, R.id.value, R.id.rebate });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	/**
	 * �˵�ѡ���
	 * δ���
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.back:
			this.finish();
			break;
			
		case R.id.mycollect:
			Intent intent = new Intent();
			//ͨ����ʽintent����MyCollerActivity
			intent.setAction("com.activity.MyCollerActivity");
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Meituan mei = meituans.get(position);
		Bundle bundle = new Bundle();
		//��mei�����л����ݸ�bundleʵ��
		bundle.putSerializable("mei", mei);
		
		Intent intent = new Intent();
		intent.putExtras(bundle);
		//ͨ����ʽintent����showdesActivity
		intent.setAction("com.activity.MainActivity01");
		startActivity(intent);
//		dl.dismiss();
	}
	
	/**
	 * ��ȡ�洢��SharedPreferences��city_postiotn������
	 */
	private void readSharedPreaferences() {
		SharedPreferences city_info = getSharedPreferences("city_info", 0);
		city_position = city_info.getInt("CITY_POSITION", 0);
	}
	
	/**
	 * ��city_position�����ݴ洢��SharedPreferences��
	 * 
	 * @param city_position  ����λ�ñ��
	 *            
	 */
	private void writeSharedPreferences(int city_position) {
		SharedPreferences city_info = getSharedPreferences("city_info", 0);
		SharedPreferences.Editor edit = city_info.edit();
		edit.putInt("CITY_POSITION", city_position);
		edit.commit();
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		writeSharedPreferences(city_position);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		writeSharedPreferences(city_position);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		exitSystem(MainActivity.this);
	}
}