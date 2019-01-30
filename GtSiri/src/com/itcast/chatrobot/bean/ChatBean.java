package com.itcast.chatrobot.bean;

import android.R.bool;

public class ChatBean {
	public String msg;
	public boolean isAsk;//聊天类型：发送or接收
	public ChatBean(String msg,boolean isAsk) {
		// TODO 自动生成的构造函数存根
		super();
		this.msg = msg;
		this.isAsk = isAsk;
	}
}
