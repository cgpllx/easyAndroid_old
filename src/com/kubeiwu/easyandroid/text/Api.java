package com.kubeiwu.easyandroid.text;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Headers;
import rx.Observable;

import com.kubeiwu.easyandroid.kretrofit.KOkClient.CacheMode;

public interface Api {
	// http://192.168.0.241/xinfang-xpt/xpt/loginProcess?j_username=13530145721&j_password=1234567&appType=android

//	@Headers(CacheMode.LOAD_CACHE_ELSE_NETWORK)
	@GET("/xpt/loginProcess?j_username=13530145721&j_password=123456&appType=Android")
	JsonResult<?> login();

//	@Headers(CacheMode.LOAD_CACHE_ELSE_NETWORK)
	@GET("/xpt/area/city")
	JsonResult<List<AreaInfo>> getCity();
	
	@Headers(CacheMode.LOAD_CACHE_ELSE_NETWORK)
	@GET("/xpt/area/city")
	Observable<JsonResult<List<AreaInfo>>> getCity1();
}
