package com.kubeiwu.easyandroid;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;
import android.app.Notification.Action;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.kubeiwu.easyandroid.kretrofit.KResult;
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
			presenter.attachView(this);
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

	}

	@Override
	public void deliverResult(int presenterId, JsonResult<List<AreaInfo>> results) {
		System.out.println("deliverResult");
		System.out.println("deliverResult--results" + results.getData().size());
		
		 GsonBuilder gb = new GsonBuilder();
		 Type type=null;
	      gb.registerTypeAdapter(type, new CustomDeserializer());
	      Gson customGson = gb.create();
	}
	public static class CustomDeserializer implements JsonDeserializer<KResult> {

	      public KResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

	         if (json == null)
	            return null;
	         else {
//	            JsonElement e = json.getAsJsonObject().get("specialStr");
	            
	            JsonElement ee= json.getAsJsonObject().get("code");
	            
	            if(ee!=null&&ee.isJsonPrimitive()&&ee.getAsString() instanceof String){
	            	String code=ee.getAsString();
	            	//如果code不为正确，就反悔父类，否则反悔子类
	            	if("c0000".equals(code)){

	            	}
	            }
//	            
//	            if (e != null && e.isJsonPrimitive() && e.getAsString() instanceof String) {
//	               CommandSpecific1 c = new CommandSpecific1();
//	               c.specialStr = e.getAsString(); // do you need this?
//	               return c;
//	            }
//
//	            e = json.getAsJsonObject().get("specialInt");
//	            if (e != null && e.isJsonPrimitive() && e.getAsNumber() instanceof Number) {
//	               CommandSpecific2 c = new CommandSpecific2();
//	               c.specialInt = e.getAsInt(); // do you need this?
//	               return c;
//	            }
	            return null; // or throw an IllegalArgumentException

	         }

	      }

	   }
	@Override
	public Observable<JsonResult<List<AreaInfo>>> onCreatObservable(int id,Bundle bundle) {
		return api.getCity1();
	}
}
