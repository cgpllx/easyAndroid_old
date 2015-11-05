/**   
 * @Title: DrawableUtil.java 
 * @Package me.pc.mobile.helper.util 
 * @Description: TODO
 * @author SilentKnight || happychinapc[at]gmail[dot]com   
 * @date 2014 2014年11月28日 上午11:39:29 
 * @version V1.0.0   
 */
package com.kubeiwu.easyandroid.easyutils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * @ClassName: DrawableUtil
 * @Description: TODO
 * @author SilentKnight || happychinapc@gmail.com
 * @date 2014年11月28日 上午11:39:29
 * 
 */
public final class BitDrawableUtil {
	private BitDrawableUtil() {
	}

	public static Drawable from(Bitmap bitmap) {
		BitmapDrawable bitDrawable = new BitmapDrawable(bitmap);
		return bitDrawable;
	}

	public static Bitmap from(Drawable drawable) {
		BitmapDrawable bitDrawable = (BitmapDrawable) drawable;
		return bitDrawable.getBitmap();
	}
}
