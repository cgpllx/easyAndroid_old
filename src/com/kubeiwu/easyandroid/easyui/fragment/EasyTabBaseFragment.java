package com.kubeiwu.easyandroid.easyui.fragment;

import java.util.List;

import android.support.v4.app.Fragment;

import com.kubeiwu.easyandroid.easyui.config.TabConfig;
import com.kubeiwu.easyandroid.easyui.pojo.EasyTab;

/**
 * @author cgpllx1@qq.com (www.kubeiwu.com)
 * @date 2014-8-20
 */
public abstract class EasyTabBaseFragment extends Fragment {

	public TabConfig onCreatTabConfig() {
		return TabConfig.getSimpleInstance();
	}

	/**
	 * Call addTab() Method To increase tab;
	 */
	public abstract List<EasyTab> onCreatTab();

	void creatTab() {
		List<EasyTab> eaTabs = onCreatTab();
		for (EasyTab eaTab : eaTabs) {
			addTab(eaTab);
		}
	}

	abstract void addTab(EasyTab eaTab);

}
