package com.kubeiwu.easyandroid.text;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import rx.Observable;

public interface Api {
	// http://192.168.0.241/xinfang-xpt/xpt/loginProcess?j_username=13530145721&j_password=1234567&appType=android

	// @Headers(CacheMode.LOAD_CACHE_ELSE_NETWORK)
	// @GET("/xpt/loginProcess?j_username=15889797548&j_password=20090705&appType=Android")
	// Call<JsonResult<?>> login();

	@GET("/xpt/loginProcess?j_username=15889797548&j_password=20090705&appType=Android")
	Call<JsonResult<String>> login();

	@GET("/xpt/loginProcess?j_username=15889797548&j_password=20090705&appType=Android")
	Observable<JsonResult<?>> login1();

	@Headers("CacheMode: ")
	@GET("/xpt/area/city")
	JsonResult<List<AreaInfo>> getCity();

	// @CacheMode(value=CacheMode.LOAD_CACHE_ELSE_NETWORK)
	// @Headers("Cache-Control : max-stale=3600")
	@Headers({ "Cache-Control: max-age=340000" //
			, "Cache-Mode:cache-else-network"//
	})
	@GET("/xpt/area/city")
	Observable<JsonResult<List<AreaInfo>>> getCity1();

	// @Multipart
	// @POST("/files")
	// Observable<JsonResult<List<AreaInfo>>> upload(@Part("fileContent")
	// TypedFile file);

	// Observable<JsonResult<List<AreaInfo>>> upload(@Body RequestBody file);
	// RequestBody photo=RequestBody .create(MediaType.parse(""), file);
	// @Multipart
	// @POST("/user/photo")
	// Call<String> updateUser(@Part("photo") RequestBody photo,
	// @Part("description") RequestBody description);
}
