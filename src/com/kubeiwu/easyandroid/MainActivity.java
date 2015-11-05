package com.kubeiwu.easyandroid;

import java.util.List;

import rx.Observable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kubeiwu.easyandroid.core.CustomDeserializer;
import com.kubeiwu.easyandroid.easyhttp.config.EAConfiguration;
import com.kubeiwu.easyandroid.easyhttp.core.retrofit.Callback;
import com.kubeiwu.easyandroid.easyhttp.core.retrofit.Response;
import com.kubeiwu.easyandroid.mvp.presenter.KSimpleNetWorkPresenter;
import com.kubeiwu.easyandroid.mvp.view.ISimpleNetWorkView;
import com.kubeiwu.easyandroid.text.Api;
import com.kubeiwu.easyandroid.text.ApiProvider;
import com.kubeiwu.easyandroid.text.AreaInfo;
import com.kubeiwu.easyandroid.text.JsonResult;

@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity implements ISimpleNetWorkView<JsonResult<List<AreaInfo>>> {
	TextView hello;
	KSimpleNetWorkPresenter<JsonResult<List<AreaInfo>>> presenter = new KSimpleNetWorkPresenter<JsonResult<List<AreaInfo>>>();
	Api api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		hello = (TextView) findViewById(R.id.hello);
		// presenter.handleError(errorDesc);

		try {
			GsonBuilder gb = new GsonBuilder();
			// Type type=null;
			gb.registerTypeAdapter(JsonResult.class, new CustomDeserializer());
			Gson customGson = gb.create();
			// KRetrofitApiFactory.getInstance().init(this );
			EasyHttpApiFactory.getInstance().init(new EAConfiguration.Builder(getApplicationContext()).setGson(customGson).build());

			// return;
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		presenter.attachView(this);
		api = ApiProvider.getInstance();
		System.out.println("111开始");
		api.login().enqueue(new Callback<JsonResult<String>>() {

			@Override
			public void onResponse(Response<JsonResult<String>> arg0) {

			}

			@Override
			public void onFailure(Throwable arg0) {

			}

			@Override
			public void onstart() {
				// TODO Auto-generated method stub

			}
		});
		// presenter.loadData();
		presenter.execute();
	}

	@Override
	public void onStart(int presenterId) {
		System.out.println("showLoading");
	}

	@Override
	public void onCompleted(int presenterId) {
		System.out.println("hideLoading");

	}

	@Override
	public void onError(int presenterId, String errorDesc) {
		System.out.println("handleError");
		Toast.makeText(getApplicationContext(), errorDesc, Toast.LENGTH_SHORT).show();

	}

	@Override
	public void deliverResult(int presenterId, JsonResult<List<AreaInfo>> results) {
		System.out.println("deliverResult");
		System.out.println("deliverResult--results" + results.getData().size());
		hello.setText(results.getData().size() + "");
		// GsonBuilder gb = new GsonBuilder();
		// Type type=null;
		// gb.registerTypeAdapter(type, new CustomDeserializer());
		// Gson customGson = gb.create();
		// ResponseBody file=new ResponseBody() {
		//
		// @Override
		// public BufferedSource source() throws IOException {
		// // TODO Auto-generated method stub
		// return null;
		// }
		//
		// @Override
		// public MediaType contentType() {
		// // TODO Auto-generated method stub
		// return null;
		// }
		//
		// @Override
		// public long contentLength() throws IOException {
		// // TODO Auto-generated method stub
		// return 0;
		// }
		// };
		// MultipartBuilder d=new MultipartBuilder();
		// d..build();
		// RequestBody.create(contentType, content)
		// RequestBody requestBody = new MultipartBuilder()
		// .type(MultipartBuilder.FORM)
		// .addPart(
		// Headers.of("Content-Disposition", "form-data; name=\"title\""),
		// RequestBody.create(null, "Square Logo"))
		// .addPart(
		// Headers.of("Content-Disposition", "form-data; name=\"image\""),
		// RequestBody.create(MEDIA_TYPE_PNG, new
		// File("website/static/logo-square.png")))
		// .build();
		// RequestBody photo=RequestBody .create(contentType, file);
	}

	@Override
	public Observable<JsonResult<List<AreaInfo>>> onCreatObservable(int id, Bundle bundle) {
		return api.getCity1();
	}
}
