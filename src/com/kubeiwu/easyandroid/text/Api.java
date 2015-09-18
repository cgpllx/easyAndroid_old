package com.kubeiwu.easyandroid.text;

import java.util.List;

import retrofit.Call;
import retrofit.KOkHttpCall.CacheMode;
import retrofit.http.GET;
import retrofit.http.Headers;
import rx.Observable;

public interface Api {
	// http://192.168.0.241/xinfang-xpt/xpt/loginProcess?j_username=13530145721&j_password=1234567&appType=android

//	@Headers(CacheMode.LOAD_CACHE_ELSE_NETWORK)
//	@GET("/xpt/loginProcess?j_username=15889797548&j_password=20090705&appType=Android")
//	Call<JsonResult<?>> login();
	
	@GET("/xpt/loginProcess?j_username=15889797548&j_password=20090705&appType=Android")
	Call<JsonResult<String>> login();
	
	@GET("/xpt/loginProcess?j_username=15889797548&j_password=20090705&appType=Android")
	Observable<JsonResult<?>> login1();

//	@Headers(CacheMode.LOAD_CACHE_ELSE_NETWORK)
	@GET("/xpt/area/city")
	JsonResult<List<AreaInfo>> getCity();
	
//	@Headers(CacheMode.LOAD_CACHE_ELSE_NETWORK)
	@GET("/xpt/area/city")
	Observable<JsonResult<List<AreaInfo>>> getCity1();
}
