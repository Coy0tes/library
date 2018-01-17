package com.jeeplus.modules.wechat;

import java.util.ArrayList;
import java.util.List;

public class WxMenu {
	List<BaseButton> button = new ArrayList<BaseButton>();

	public List<BaseButton> getButton() {
		return button;
	}

	public void setButton(List<BaseButton> button) {
		this.button = button;
	}
}
