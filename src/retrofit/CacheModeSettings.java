package retrofit;

public interface CacheModeSettings {
	// 网络
	int LOAD_DEFAULT = -1;
	// 缓存---->网络
	int LOAD_CACHE_ELSE_NETWORK = 1;
	// 网络---->缓存
	int LOAD_NETWORK_ELSE_CACHE = 2;
	// 网络
	int LOAD_NETWORK_ONLY = 3;
}
