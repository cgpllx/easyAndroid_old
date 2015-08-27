package com.kubeiwu.httphelper;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.kubeiwu.DisLrucache.R;
import com.kubeiwu.httphelper.retrofit.KRetrofitApiFactory;
import com.kubeiwu.httphelper.text.Api;
import com.kubeiwu.httphelper.text.AreaInfo;
import com.kubeiwu.httphelper.text.JsonResult;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	TextView hello;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		hello=(TextView) findViewById(R.id.hello);
		try {
			KRetrofitApiFactory.getInstance().init(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final Api api = KRetrofitApiFactory.getInstance().getApi(Api.class, "http://xf.qfang.com");
		new Thread() {
			public void run() {
				System.out.println("开始");
		
				try {
					JsonResult<List<AreaInfo>> list = api.getCity();

					final List<AreaInfo> areaInfos = list.getData();
					for (AreaInfo areaInfo : areaInfos) {
						list.getData().get(0).getName();
						System.out.println(areaInfo.getName());
					}
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// System.out.println(d);
				try {
					final JsonResult<?> d = api.login();
					runOnUiThread(new   Runnable() {
						public void run() {
							hello.setText(new Gson().toJson(d));	
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
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
