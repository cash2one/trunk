package com.shandagames.android.fragment;

import java.util.Collection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shandagames.android.app.BaseListFragment;
import com.shandagames.android.support.IntentSupport;
import com.shandagames.android.util.ToastUtil;

public class WidgetFragment extends BaseListFragment implements OnItemClickListener {

	private ListView listView;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listView = getListView();
		listView.setBackgroundColor(Color.WHITE);
		listView.setOnItemClickListener(this);
		
		String[] data = {"读取Json数据", "应用程序管理", "读取Gtalk联系人", "GridLayout布局"};
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, data);
		setListAdapter(mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 0:
			parseJson();
			break;
		case 1:
			startActivity(IntentSupport.newManageApplicationIntent());
			break;
		case 2:
			connectGtalk();
			break;
		case 3:
			UnsaveDialogFragment unSaveDialogFragment = new UnsaveDialogFragment();
			unSaveDialogFragment.show(getChildFragmentManager(), unSaveDialogFragment.getClass().getName());
			break;
		}
	}
	
	private void parseJson() {
		setProgressBarIndeterminateVisibility(true);
		final RequestQueue volleyQueue = Volley.newRequestQueue(getActivity());
		String url = "http://jsonview.com/example.json";
		volleyQueue.add(new StringRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				setProgressBarIndeterminateVisibility(false);
				Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				volleyQueue.cancelAll(this);
				setProgressBarIndeterminateVisibility(false);
				Toast.makeText(getActivity(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
			}
		}));
	}
	
	private void connectGtalk() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//指定Gtalk服务器地址和端口号
					ConnectionConfiguration configuration = new ConnectionConfiguration(
							"talk.google.com", 5222, "gmail.com");
					//根据配置的地址和端口号创建xmpp对象
					XMPPConnection xmppConnection = new XMPPConnection(configuration);
					// 连接gtalk服务器
					xmppConnection.connect(); 
					// 使用账号和密码登陆服务器
					xmppConnection.login("dreamxsky@gmail.com", "5340Selience");
					
					Presence presence = new Presence(Presence.Type.available);
					//登陆成功向gtalk服务器发送一条消息，表明当前处于活动状态
					xmppConnection.sendPacket(presence);
					
					StringBuilder sb = new StringBuilder();
					Collection<RosterEntry> entries = xmppConnection.getRoster().getEntries();
					for (RosterEntry entry : entries) {
						sb.append(entry.getName()+":"+entry.getUser()+"\n");
					}
					if (sb.length()>0) sb=sb.deleteCharAt(sb.length()-1);
					
					ToastUtil.showMessage(getActivity(), sb.toString());
				} catch (XMPPException ex) {
					ToastUtil.showMessage(getActivity(), "登陆失败");
				}
			}
		}).start();
	}
}
