package com.kubeiwu.httphelper;

import java.io.IOException;
import java.util.List;

import com.kubeiwu.DisLrucache.R;
import com.kubeiwu.httphelper.retrofit.KRetrofitApiFactory;
import com.kubeiwu.httphelper.text.Api;
import com.kubeiwu.httphelper.text.AreaInfo;
import com.kubeiwu.httphelper.text.JsonResult;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			KRetrofitApiFactory.getInstance().init(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final Api api = KRetrofitApiFactory.getInstance().getApi(Api.class, "http://192.168.0.241/xinfang-xpt");
		new Thread() {
			public void run() {
				System.out.println("开始");
				try {
					JsonResult<?> d = api.login();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					JsonResult<List<AreaInfo>> list=api.getCity();
					
					List<AreaInfo> areaInfos=list.getData();
					for(AreaInfo areaInfo:areaInfos){
						list.getData().get(0).getName();
						System.out.println(areaInfo.getName());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				System.out.println(d);
			};
		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
