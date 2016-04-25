package com.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MenuActivity extends Activity {
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.back:
			this.finish();
			break;
			
		case R.id.back_main:
			Intent intent2 = new Intent();
			intent2.setClass(this, MainActivity.class);
			startActivity(intent2);
			
			break;

		case R.id.mycollect:
			Intent intent = new Intent();
			intent.setAction("com.activity.MyCollectActivity");
			startActivity(intent);
			break;

		case R.id.about:
			AlertDialog.Builder dialog = new Builder(this);
			dialog.setTitle(R.string.author);
			// װ��/res/layout/author.xml
			final LinearLayout author = (LinearLayout) getLayoutInflater().inflate(
					R.layout.author, null);
			

			dialog.setView(author);
			dialog.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							
							EditText edit_suggest = (EditText)author.findViewById(R.id.edit_suggest);
							String suggest = edit_suggest.getText().toString();
							try
							{
								final Intent emailIntent = new Intent(
										android.content.Intent.ACTION_SEND);
								emailIntent.setType("plain/text");
								emailIntent.putExtra(
										android.content.Intent.EXTRA_EMAIL,
										"52017889@qq.com");
								emailIntent.putExtra(
										android.content.Intent.EXTRA_SUBJECT,
										"�Ź��ͻ������");
								emailIntent.putExtra(
										android.content.Intent.EXTRA_TEXT,
										suggest);
								startActivity(Intent.createChooser(emailIntent,
										"�ʼ�������...."));
							} catch (Exception e)
							{
								Toast.makeText(MenuActivity.this,
										"���������ֻ�����ʼ���ַ", 1).show();
							}

						}
					});
			dialog.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener()	//���ü���
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();		//dialog��ʧ

						}
					});
			dialog.show();

			break;

		case R.id.eixt:
			System.out.println("exit");
			// ActivityManager am = (ActivityManager)
			// getSystemService(ACTIVITY_SERVICE);
			// am.killBackgroundProcesses("com.kang.tuangou");
			exitSystem(MenuActivity.this);
			break;

		}

		return true;
	}
	
	protected void exitSystem(Context context){
		AlertDialog.Builder dialog = new Builder(context);
		dialog.setTitle("ȷ���˳�")
		.setMessage("ȷ���˳���")
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent1 = new Intent(Intent.ACTION_MAIN);
				intent1.addCategory(Intent.CATEGORY_HOME);
				intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent1);
			}
		}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}
}
