package retrofit.http;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import retrofit.CacheModeSettings;

@Documented
@Target(METHOD)
@Retention(RUNTIME)
/**
 * 请使用CacheSettings 中的常量
 * @author Administrator
 *
 */
public @interface CacheMode {

	int value() default CacheModeSettings.LOAD_DEFAULT;
}
