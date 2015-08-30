package com.kubeiwu.httphelper;

import java.io.IOException;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kubeiwu.DisLrucache.R;
import com.kubeiwu.httphelper.mvp.presenter.KSimpleNetWorkPresenter;
import com.kubeiwu.httphelper.mvp.view.ISimpleView;
import com.kubeiwu.httphelper.mvp.view.IView;
import com.kubeiwu.httphelper.retrofit.KRetrofitApiFactory;
import com.kubeiwu.httphelper.text.Api;
import com.kubeiwu.httphelper.text.ApiPrivider;
import com.kubeiwu.httphelper.text.AreaInfo;
import com.kubeiwu.httphelper.text.JsonResult;

public class MainActivity extends ActionBarActivity implements ISimpleView<JsonResult<List<AreaInfo>>> {
	TextView hello;
	KSimpleNetWorkPresenter<JsonResult<List<AreaInfo>>> presenter = new KSimpleNetWorkPresenter<JsonResult<List<AreaInfo>>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		hello = (TextView) findViewById(R.id.hello);

		try {
			KRetrofitApiFactory.getInstance().init(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		presenter.setView(this);
		final Api api = ApiPrivider.getInstance();
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
					runOnUiThread(new Runnable() {
						public void run() {
							hello.setText(new Gson().toJson(d));
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
		presenter.loadData(api.getCity1().cache());
	}

	@Override
	public void showLoading(int presenterId) {
		System.out.println("showLoading");
	}

	@Override
	public void hideLoading(int presenterId) {
		System.out.println("hideLoading");

	}

	@Override
	public void handleError(int presenterId, String errorDesc) {
		System.out.println("handleError");

	}

	@Override
	public void deliverResult(int presenterId, JsonResult<List<AreaInfo>> results) {
		System.out.println("deliverResult");
		System.out.println("deliverResult--results" + results.getData().size());
	}
}
