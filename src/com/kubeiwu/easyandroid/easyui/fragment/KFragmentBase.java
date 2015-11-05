//2014-8-20
package com.kubeiwu.easyandroid.easyui.fragment;

import android.support.v4.app.Fragment;

/**
 * @author cgpllx1@qq.com (www.kubeiwu.com)
 * @date 2014-8-20
 */
public abstract class KFragmentBase extends Fragment {

	public TabConfig getTabConfig() {
		return null;
	}

	public static class TabConfig {
		public interface TabGravity {
			int BOTTOM = 0, TOP = 1;
		}

		private int widgetBackgroundResource ;
		private int widgetDividerDrawableResId;
		public int getWidgetDividerDrawableResId() {
			return widgetDividerDrawableResId;
		}

		public TabConfig setWidgetDividerDrawableResId(int widgetDividerDrawableResId) {
			this.widgetDividerDrawableResId = widgetDividerDrawableResId;
			return this;
		}

		private int tabGravity = TabGravity.BOTTOM;

		public int getTabGravity() {
			return tabGravity;
		}

		public static TabConfig getSimpleInstance() {
			return new TabConfig();
		}

		public void setGravity(int tabGravity) {
			this.tabGravity = tabGravity;
		}

		public int getWidgetBackgroundResource() {
			return widgetBackgroundResource;
		}

		public TabConfig setWidgetBackgroundResource(int mWidgetBackgroundResource) {
			this.widgetBackgroundResource = mWidgetBackgroundResource;
			return this;
		}
	}
}
