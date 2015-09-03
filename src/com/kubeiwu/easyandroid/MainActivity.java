package com.kubeiwu.easyandroid;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kubeiwu.easyandroid.kretrofit.KRetrofitApiFactory;
import com.kubeiwu.easyandroid.mvp.presenter.KSimpleNetWorkPresenter;
import com.kubeiwu.easyandroid.mvp.view.ISimpleNetWorkView;
import com.kubeiwu.easyandroid.text.Api;
import com.kubeiwu.easyandroid.text.ApiPrivider;
import com.kubeiwu.easyandroid.text.AreaInfo;
import com.kubeiwu.easyandroid.text.JsonResult;

public class MainActivity extends FragmentActivity implements ISimpleNetWorkView<JsonResult<List<AreaInfo>>> {
	TextView hello;
	KSimpleNetWorkPresenter<JsonResult<List<AreaInfo>>> presenter = new KSimpleNetWorkPresenter<JsonResult<List<AreaInfo>>>();
	  Api api;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		hello = (TextView) findViewById(R.id.hello);
//		presenter.handleError(errorDesc);
		 
			try {
				KRetrofitApiFactory.getInstance().init(this);
				
//				return;
			} catch (IOException e1) {
				 
				e1.printStackTrace();
			}
			presenter.setView(this);
			  api = ApiPrivider.getInstance();
			System.out.println("111开始");
		new Thread() {
			@Override
			public void run() {
				System.out.println("1122开始");
				try {
					final JsonResult<?> d = api.login();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							hello.setText(new Gson().toJson(d));
							System.out.println("1122333开始");
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("1122333444开始");
				}
				
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
		
			};
		}.start();
		presenter.loadData();
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

	@Override
	public Observable<JsonResult<List<AreaInfo>>> onCreatObservable() {
		return api.getCity1();
	}
}
