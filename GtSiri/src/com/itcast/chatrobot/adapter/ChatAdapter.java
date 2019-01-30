package com.itcast.chatrobot.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itcast.chatrobot.R;
import com.itcast.chatrobot.bean.ChatBean;
import com.itcast.chatrobot.listener.OnSpeakListener;

public class ChatAdapter  extends BaseAdapter {

	private Context mContext;
	private ArrayList mList;
	private OnSpeakListener listener;
	public ChatAdapter() {
		// TODO 自动生成的构造函数存根
		super();
	}
	public ChatAdapter(Context mContext,ArrayList mList) {
		// TODO 自动生成的构造函数存根
		super();
		this.mContext = mContext;
		this.mList = mList;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.list_item, null);

			holder = new ViewHolder();
			holder.tvAsk = (TextView) convertView.findViewById(R.id.tv_ask);
			holder.tvAnswer = (TextView) convertView
					.findViewById(R.id.tv_answer);
			holder.llAnswer = (LinearLayout) convertView.findViewById(R.id.ll_answer);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ChatBean item = (ChatBean) mList.get(position);

		if (item.isAsk) {
			//提问
			holder.tvAsk.setVisibility(View.VISIBLE);
			holder.tvAsk.setText(item.msg);
			holder.llAnswer.setVisibility(View.GONE);
			final String msg = item.msg;
			holder.tvAsk.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					if(listener!=null){
						listener.toSpeak(msg);
					}
				}
			});
		} else {
			//回答
			holder.tvAsk.setVisibility(View.GONE);
			holder.llAnswer.setVisibility(View.VISIBLE);
			holder.tvAnswer.setText(item.msg);
			final String msg = item.msg;
			holder.tvAnswer.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					if(listener!=null){
						listener.toSpeak(msg);
					}
				}
			});
		}

		return convertView;
	}
	public void setListener(OnSpeakListener listener) {
		this.listener = listener;
	}

	class ViewHolder {
		public TextView tvAnswer;
		public TextView tvAsk;
		public LinearLayout llAnswer;
	}

}
