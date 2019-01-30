package com.itcast.chatrobot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.itcast.chatrobot.adapter.ChatAdapter;
import com.itcast.chatrobot.bean.ChatBean;
import com.itcast.chatrobot.bean.VoiceBean;
import com.itcast.chatrobot.listener.OnSpeakListener;
import com.itcast.chatrobot.util.Config;


public class MainActivity extends ActionBarActivity implements OnClickListener{

	private Button send;
	private ImageView speak;
	private EditText content;
	private ListView listview;
	private ChatAdapter chatAdapter;
	private ArrayList<ChatBean> mList;
	private StringBuffer mBuffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mList = new ArrayList<ChatBean>();
        chatAdapter = new ChatAdapter(this,mList);
        chatAdapter.setListener(new OnSpeakListener() {
			
			@Override
			public void toSpeak(String msg) {
				// TODO �Զ����ɵķ������
				startSpeak(msg);
			}
		});
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + Config.app_id);
        initViews();
        setListeners();
    }


    private void setListeners() {
		// TODO �Զ����ɵķ������
		send.setOnClickListener(this);
		speak.setOnClickListener(this);
	}


	private void initViews() {
		// TODO �Զ����ɵķ������
		send = (Button) findViewById(R.id.send);
		speak = (ImageView) findViewById(R.id.speak);
		content = (EditText) findViewById(R.id.content);
		listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(chatAdapter);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	@Override
	public void onClick(View view) {
		// TODO �Զ����ɵķ������
		switch (view.getId()) {
		case R.id.send:
			//����
			sendMsg();
			break;
		case R.id.speak:
			//����
			startListen();
			break;

		default:
			break;
		}
	}


	private void sendMsg() {
		// TODO �Զ����ɵķ������
		String askContent = content.getText().toString().trim();
		if(!TextUtils.isEmpty(askContent)){
			ChatBean cb = new ChatBean(askContent, true);
			mList.add(cb);
			answer(askContent);
			chatAdapter.notifyDataSetChanged();
			content.setText("");
		}
	}


	//��ʼ����������Ϣ
	private void startListen() {
		// TODO �Զ����ɵķ������
		RecognizerDialog mDialog = new RecognizerDialog(this, null);
		mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
		mBuffer = new StringBuffer();
		mDialog.setListener(new RecognizerDialogListener() {
			@Override
			public void onResult(RecognizerResult results, boolean isLast) {
				String result = results.getResultString();
				String parseData = parseData(result);
				mBuffer.append(parseData);
				if (isLast) {
					//�Ự����
					String askContent = mBuffer.toString();
					ChatBean cb = new ChatBean(askContent, true);
					mList.add(cb);
					answer(askContent);		
					chatAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onError(SpeechError arg0) {

			}

		});
		mDialog.show();
	}
	//��Ϣ����
	private void answer(String askContent) {
		// TODO �Զ����ɵķ������
		if(askContent.contains("����")&&(askContent.contains("����")||askContent.contains("���¼���")||askContent.contains("���¼���"))){
			SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd��");
			Date date = new Date(System.currentTimeMillis());
			String dateStr = format.format(date);
			ChatBean cb = new ChatBean("������"+dateStr, false);
			mList.add(cb);
			startSpeak("������"+dateStr);
		}else
		if(askContent.contains("��ǰʱ��")){
			SimpleDateFormat format = new SimpleDateFormat("HH��mm��ss��");
			Date date = new Date(System.currentTimeMillis());
			String timeStr = format.format(date);
			ChatBean cb = new ChatBean("��ǰʱ��"+timeStr, false);
			mList.add(cb);
			startSpeak("��ǰʱ��"+timeStr);
		}else{
			ChatBean cb = new ChatBean(Config.err_msg, false);
			mList.add(cb);
			startSpeak(Config.err_msg);
		}
	}
	protected String parseData(String json) {
		//Gson google
		Gson gson = new Gson();
		VoiceBean voiceBean = gson.fromJson(json, VoiceBean.class);

		StringBuffer sb = new StringBuffer();

		ArrayList<com.itcast.chatrobot.bean.VoiceBean.WS> ws = voiceBean.ws;
		for (com.itcast.chatrobot.bean.VoiceBean.WS w : ws) {
			String word = w.cw.get(0).w;
			sb.append(word);
		}

		return sb.toString();
	}
	//�ϳ���������
	public void startSpeak(String content) {
		SpeechSynthesizer mTts = SpeechSynthesizer
				.createSynthesizer(this, null);
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan"); //���÷�����
		mTts.setParameter(SpeechConstant.SPEED, "50");//��������
		mTts.setParameter(SpeechConstant.VOLUME, "80");//������������Χ 0~100
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
		//3.��ʼ�ϳ�
		mTts.startSpeaking(content, null);
	}
}
