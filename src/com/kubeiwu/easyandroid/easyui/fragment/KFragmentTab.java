package com.kubeiwu.easyandroid.easyui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kubeiwu.easyandroid.easyui.EasyR;
import com.kubeiwu.easyandroid.easyui.config.TabConfig;
import com.kubeiwu.easyandroid.easyui.utils.ViewFactory;

/**
 * Tab+Fragment
 * 
 * @author 耳东
 *
 */
public abstract class KFragmentTab extends KFragmentBase {
	protected FragmentTabHost mFragmentTabHost;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		TabConfig tabConfig = getTabConfig();

		if (tabConfig == null) {
			tabConfig = TabConfig.getSimpleInstance();
		}
		View view = ViewFactory.getFragmentTabHostView(inflater.getContext(), tabConfig.getTabGravity());
		mFragmentTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
		mFragmentTabHost.setup(view.getContext(), getChildFragmentManager(), EasyR.id.realtabcontent);
		initTab(mFragmentTabHost);
		int tabcount = mFragmentTabHost.getTabWidget().getChildCount();
		if (tabcount == 0) {
			throw new IllegalArgumentException("Please in the initTab method to add Tab Fragment");
		}
		mFragmentTabHost.getTabWidget().setBackgroundResource(tabConfig.getWidgetBackgroundResource());
		return view;
	}

	/**
	 * eg:mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator( getTabItemView(0)), IndexFragment.class, null);
	 */
	protected abstract void initTab(FragmentTabHost mTabHost);
}
