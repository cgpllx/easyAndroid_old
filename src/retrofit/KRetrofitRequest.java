package retrofit;

public class KRetrofitRequest {
	private final com.squareup.okhttp.Request request;
	private final int cacheMode;

	public KRetrofitRequest(com.squareup.okhttp.Request request, int cacheMode) {
		this.request = request;
		this.cacheMode = cacheMode;
	}

	public com.squareup.okhttp.Request getRequest() {
		return request;
	}

	public int getCacheMode() {
		return cacheMode;
	}

}
