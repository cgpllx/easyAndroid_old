package com.kubeiwu.easyandroid.easyutils;

import java.util.List;
import java.util.Map;

import android.util.SparseArray;

public class ArrayUtils {

	public static <T> boolean isEmpty(List<T> array) {
		if (array == null || array.size() == 0)
			return true;
		else
			return false;
	}

	public static <T> boolean isEmpty(SparseArray<T> array) {
		if (array == null || array.size() == 0)
			return true;
		else
			return false;
	}

	public static <E, T> boolean isEmpty(Map<T, E> array) {
		if (array == null || array.size() == 0)
			return true;
		else
			return false;
	}
 
}
