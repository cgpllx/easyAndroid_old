package com.kubeiwu.easyandroid.easyui.utils;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.kubeiwu.easyandroid.easyui.EasyR;
import com.kubeiwu.easyandroid.easyui.config.TabConfig.TabGravity;
import com.kubeiwu.easyandroid.easyui.view.KFragmentTabHostSaveState;

/**
 * @author 耳东 www.kubeiwu.com
 *
 */
public class ViewFactory {
	/**
	 * 获取TabHost
	 * 
	 * @param mContext
	 * @param gravity
	 *            tabs的位置，上面TOP 下面BOTTOM，目前只支持两个
	 * @return
	 */
	public static KFragmentTabHostSaveState getKFragmentTabHostSaveStateView(Context mContext, int gravity) {
		// init FragmentTabHost
		KFragmentTabHostSaveState tabhost = new KFragmentTabHostSaveState(mContext);
		tabhost.setId(android.R.id.tabhost);
		tabhost.setLayoutParams(new FragmentTabHost.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		LinearLayout mLinearLayout = new LinearLayout(mContext);
		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		// init FrameLayout
		FrameLayout realtabcontent = new FrameLayout(mContext);
		realtabcontent.setId(EasyR.id.realtabcontent);
		realtabcontent.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));

		// init TabWidget
		TabWidget tabs = new TabWidget(mContext);
		tabs.setId(android.R.id.tabs);
		tabs.setLayoutParams(new TabWidget.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0));
		tabs.setOrientation(LinearLayout.HORIZONTAL);

		FrameLayout tabcontent = new FrameLayout(mContext);
		tabcontent.setId(android.R.id.tabcontent);
		tabcontent.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 0f));

		tabhost.addView(mLinearLayout);
		switch (gravity) {
		case TabGravity.BOTTOM:
			mLinearLayout.addView(realtabcontent);

			mLinearLayout.addView(tabs);// wiget在下面
			mLinearLayout.addView(tabcontent);
			break;
		case TabGravity.TOP:
			mLinearLayout.addView(tabs);// wiget在上面
			mLinearLayout.addView(tabcontent);

			mLinearLayout.addView(realtabcontent);
			break;
		}
		return tabhost;
	}
	/**
	 * 获取TabHost
	 * 
	 * @param mContext
	 * @param gravity
	 *            tabs的位置，上面TOP 下面BOTTOM，目前只支持两个
	 * @return
	 */
	public static FragmentTabHost getFragmentTabHostView(Context mContext, int gravity) {
		// init FragmentTabHost
		FragmentTabHost tabhost = new FragmentTabHost(mContext);
		tabhost.setId(android.R.id.tabhost);
		tabhost.setLayoutParams(new FragmentTabHost.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		LinearLayout mLinearLayout = new LinearLayout(mContext);
		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		// init FrameLayout
		FrameLayout realtabcontent = new FrameLayout(mContext);
		realtabcontent.setId(EasyR.id.realtabcontent);
		realtabcontent.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
		
		// init TabWidget
		TabWidget tabs = new TabWidget(mContext);
		tabs.setId(android.R.id.tabs);
		tabs.setLayoutParams(new TabWidget.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0));
		tabs.setOrientation(LinearLayout.HORIZONTAL);
		
		FrameLayout tabcontent = new FrameLayout(mContext);
		tabcontent.setId(android.R.id.tabcontent);
		tabcontent.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 0f));
		
		tabhost.addView(mLinearLayout);
		switch (gravity) {
			case TabGravity.BOTTOM:
				mLinearLayout.addView(realtabcontent);
				
				mLinearLayout.addView(tabs);// wiget在下面
				mLinearLayout.addView(tabcontent);
				break;
			case TabGravity.TOP:
				mLinearLayout.addView(tabs);// wiget在上面
				mLinearLayout.addView(tabcontent);
				
				mLinearLayout.addView(realtabcontent);
				break;
		}
		return tabhost;
	}

	/**
	 * 获取TabHost
	 * 
	 * @param mContext
	 * @param gravity
	 *            tabs的位置，上面TOP 下面BOTTOM，目前只支持两个
	 * @return
	 */
	public static TabHost getTabHostAndPagerView(Context mContext, int tabGravity) {
		// init TabHost
		TabHost tabhost = new TabHost(mContext);
		tabhost.setId(android.R.id.tabhost);
		tabhost.setLayoutParams(new FragmentTabHost.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		LinearLayout mLinearLayout = new LinearLayout(mContext);
		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// init TabWidget
		TabWidget tabs = new TabWidget(mContext);
		tabs.setId(android.R.id.tabs);
		tabs.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0));
		tabs.setOrientation(LinearLayout.HORIZONTAL);

		FrameLayout tabcontent = new FrameLayout(mContext);
		tabcontent.setId(android.R.id.tabcontent);
		tabcontent.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 0f));

		ViewPager viewPager = new ViewPager(mContext);
		viewPager.setId(EasyR.id.pager);
		viewPager.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));

		tabhost.addView(mLinearLayout);
		switch (tabGravity) {
		case TabGravity.BOTTOM:
			mLinearLayout.addView(viewPager);

			mLinearLayout.addView(tabs);// wiget在下面
			mLinearLayout.addView(tabcontent);
			break;
		case TabGravity.TOP:
		default:
			mLinearLayout.addView(tabs);// wiget在上面
			mLinearLayout.addView(tabcontent);

			mLinearLayout.addView(viewPager);
			break;
		}
		return tabhost;
	}
}
