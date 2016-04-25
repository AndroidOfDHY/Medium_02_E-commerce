package com.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MyMapActivity extends MapActivity {

	private Drawable posDrawable;
	private MapView mapView;
	private String shop_name;
	private String shop_tel;
	private String shop_addr;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏

		setContentView(R.layout.map);

		Intent intent = getIntent();
		String shop_long = intent.getStringExtra("shop_long");
		String shop_lat = intent.getStringExtra("shop_lat");
		shop_name = intent.getStringExtra("shop_name");
		shop_tel = intent.getStringExtra("shop_tel");
		shop_addr = intent.getStringExtra("shop_addr");

		TextView title = (TextView) findViewById(R.id.shop_name);
		title.setText(shop_name);

		Button back_bt = (Button) findViewById(R.id.map_back);
		back_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		posDrawable = this.getResources().getDrawable(R.drawable.pos);

		mapView = (MapView) findViewById(R.id.mapview);

		// 设置显示放大,缩小的控制按钮
		mapView.setBuiltInZoomControls(true);

		double dLong = Double.valueOf(shop_long);
		double dLat = Double.valueOf(shop_lat);

		// 调用方法更新MapView
		updateMapView(dLong, dLat);

	}

	private void updateMapView(double dLong, double dLat) {
		GeoPoint gp = new GeoPoint((int) (dLong * 1E6), (int) (dLat * 1E6));

		mapView.displayZoomControls(true);

		List<Overlay> mapOverlays = mapView.getOverlays();

		CustomItemizedOverlay itemizedOverlay = new CustomItemizedOverlay(
				posDrawable, this);

		OverlayItem overlayitem = new OverlayItem(gp, shop_name, "电话:"
				+ shop_tel + "\n地址:" + shop_addr);

		itemizedOverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedOverlay);

		MapController mapController = mapView.getController();

		mapController.animateTo(gp);

		mapController.setZoom(17);

	}

	@Override
	protected boolean isRouteDisplayed() {
		return true;
	}

	/**
	 * 客户自定义显示层
	 * 
	 * @author Administrator
	 * 
	 */
	public class CustomItemizedOverlay extends ItemizedOverlay<OverlayItem> {

		private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();

		private Context context;

		public CustomItemizedOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
		}

		public CustomItemizedOverlay(Drawable defaultMarker, Context context) {
			this(defaultMarker);
			this.context = context;
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mapOverlays.get(i);
		}

		@Override
		public int size() {
			return mapOverlays.size();
		}

		@Override
		protected boolean onTap(int index) {
			OverlayItem item = mapOverlays.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.show();
			return true;
		}

		public void addOverlay(OverlayItem overlay) {
			mapOverlays.add(overlay);
			this.populate();
		}

	}

}
