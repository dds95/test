package cn.com.shoulou.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.com.shoulou.MainActivity;
import cn.com.shoulou.R;
import cn.com.shoulou.TuoKeMainActivity;
import cn.com.shoulou.demain.TBean;
import cn.com.shoulou.util.WebServiceUtils.WebServiceCallBack;

public class TKMorePopWindow extends PopupWindow   {
	private View conentView;
	private LinearLayout ll;
	private LinearLayout ll2;
	private Context context;
	private LinearLayout ll3;
//	private LinearLayout ll4;
	private TextView tv1,tv2;
	private List<TBean> beans;
	private ListView lv;
	public TKMorePopWindow(final Activity context) {
		this.context=context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.more_popup_dialog2, null);
		init();
		load();
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth()/3;
		this.setContentView(conentView);
		this.setWidth(w);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
		// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		this.setAnimationStyle(R.style.AnimationPreview);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MyPreference.getInstance(context).setPcode(beans.get(position).getId());
				MyPreference.getInstance(context).setPcodeName(beans.get(position).getTheme());
				TuoKeMainActivity.setTitle(beans.get(position).getTheme());
				Utils.loadpic2(context, TuoKeMainActivity.adapter,TuoKeMainActivity.ag);
				TKMorePopWindow.this.dismiss();
			}
		});
	}

	private void load() {
		HashMap<String, String> mp = new HashMap<>();
		mp.put("emplid", "");
		WebServiceUtils.callWebService(context, "ProjectList", mp, new WebServiceCallBack() {
			
			@Override
			public void callBack(Object result) {
				
				if(result!=null){
					try {
						
						JSONObject json = new JSONObject(result.toString());
						for(int i=0;i<json.getJSONArray("RecordSet").length();i++){
							TBean bean = new TBean();
							bean.setId(json.getJSONArray("RecordSet").getJSONObject(i).getString("proj_code"));
							bean.setTheme(json.getJSONArray("RecordSet").getJSONObject(i).getString("proj_name"));
							beans.add(bean);
						}
						lv.setAdapter(new lvadapter(beans));
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
			}
		});
	}

	private void init() {
			lv = (ListView) conentView.findViewById(R.id.listView1);
			beans= new ArrayList<TBean>();
	}

	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
		} else {
			this.dismiss();
		}
	}

	
	class lvadapter extends BaseAdapter{

		private List<TBean> b;

		public lvadapter(List<TBean> beans) {
			this.b = beans;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return b.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return b.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			convertView=View.inflate(context, R.layout.lv_main, null);
			TextView tv = (TextView) convertView.findViewById(R.id.textView1);
			tv.setText(b.get(position).getTheme());
			
			return convertView;
		}
		
	}
}
