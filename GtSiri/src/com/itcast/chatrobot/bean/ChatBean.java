package com.itcast.chatrobot.bean;

import android.R.bool;

public class ChatBean {
	public String msg;
	public boolean isAsk;//�������ͣ�����or����
	public ChatBean(String msg,boolean isAsk) {
		// TODO �Զ����ɵĹ��캯�����
		super();
		this.msg = msg;
		this.isAsk = isAsk;
	}
}
