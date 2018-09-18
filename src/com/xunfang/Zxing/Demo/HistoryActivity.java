package com.xunfang.Zxing.Demo;


import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.xunfang.Zxing.db.BarcodeDBUtil;
import com.xunfang.Zxing.util.BarcodeBean;
import com.xunfang.Zxing.util.Tools;

public class HistoryActivity extends Activity implements OnClickListener,OnItemLongClickListener,OnItemClickListener{
	//定义控件
	private ImageView revert;//"返回"按钮
	private TextView title;//标题
	private ListView barcodeListView;
	private ProgressDialog dialog;//进度
	
	private List<BarcodeBean> list = null;
	private BarcodeAdapter adapter = null;
	private BarcodeBean barcodeBean;
	private int location;
	String operateStr = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//设置窗口布局
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.history);
        //自定义窗口样式
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.common_title);
		revert = (ImageView)findViewById(R.id.common_title_revert);
		title = (TextView)findViewById(R.id.common_title_title);
		title.setText(R.string.history);
		revert.setOnClickListener(this);
		barcodeListView = (ListView)findViewById(R.id.barcode_list_content);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new GetSavedBarcodeTask().execute("");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view instanceof ImageView || view instanceof Button){
			switch (view.getId()) {
			case R.id.common_title_revert:
				this.finish();
				break;
			default:
				break;
			}
		}
	}
	/**
	 * 异步操作类——执行更新二维码扫描记录的操作
	 * @author sas
	 *
	 */
	class GetSavedBarcodeTask extends AsyncTask<String, Integer, String>{

		/**
		 * 后台执行查询保存的二维码扫描记录
		 */
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			BarcodeDBUtil barcodeDBUtil = new BarcodeDBUtil(HistoryActivity.this);
			list = barcodeDBUtil.getBarcodeRecordList();
			System.out.println("list.size()--->"+list.size());
			if(list != null) return "true";
			else return "false";
		}
		/**
		 * 
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if("true".equals(result)){
				if(list==null||list.size()==0){
					Toast.makeText(HistoryActivity.this, "历史记录为空", Toast.LENGTH_SHORT).show();
				}else {
					//实例化适配器
					adapter = new BarcodeAdapter(HistoryActivity.this, list);
					//为ListView添加适配器
					barcodeListView.setAdapter(adapter);
					//为ListView设置监听器
					barcodeListView.setOnItemClickListener(HistoryActivity.this);
					barcodeListView.setOnItemLongClickListener(HistoryActivity.this);
				}
			}else{
				Toast.makeText(HistoryActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
			}
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = ProgressDialog.show(HistoryActivity.this,null,"正在查询…",true);
		}
	}
	/**
	 * 自定义适配器类*/
	class BarcodeAdapter extends BaseAdapter{

		private Context mContext;   
        private LayoutInflater mInflater; 
        private List<BarcodeBean> list ;
        
        public BarcodeAdapter(Context content,List<BarcodeBean> list){
        	this.mContext = content;
        	this.mInflater = LayoutInflater.from(mContext);
        	this.list = list;
        }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public BarcodeBean getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();   
                convertView = mInflater.inflate(R.layout.barcode_item, null);   
                holder.barcode = (TextView) convertView.findViewById(R.id.barcode_item_value);   
                holder.savetime = (TextView) convertView.findViewById(R.id.barcode_item_savetime);   
                convertView.setTag(holder);  
			}else holder = (ViewHolder)convertView.getTag();
				
			BarcodeBean type = list.get(position);
			holder.barcode.setText(type.getBarcode());
			holder.savetime.setText(type.getSavetime());
			return convertView;
		}
		
	} 
	static final class ViewHolder{
		TextView id;
		TextView barcode;
		TextView savetime;
	}
	/**
	 * ListView中的选项被长按时的回调方法*/
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
		
		//view.setBackgroundResource(R.drawable.main_row_stat_pressed);
		AlertDialog.Builder alertdb=new AlertDialog.Builder(HistoryActivity.this);
		//选中项的位置
		location = position;
		//获得选中项上的大类对象
        barcodeBean = (BarcodeBean)list.get(position);
        //设置标题
        alertdb.setTitle("选择操作");
        alertdb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//parent.setBackgroundResource(R.drawable.main_row_stat_normal);
			}
		});
        if(Tools.isURL(barcodeBean.getBarcode())){
        	operateStr = "访问";
        }else{
        	operateStr = "搜索";
        }
        final CharSequence[] items = {operateStr,"删除"};
        alertdb.setItems(items, new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int item) { 
            	if(item==0){//选择访问或搜索
            		if("搜索".equals(operateStr)){
            			Intent intent = new Intent(); 
    					intent.setAction(Intent.ACTION_WEB_SEARCH); 
    					intent.putExtra(SearchManager.QUERY,barcodeBean.getBarcode()); 
    					startActivity(intent);
            		}else if("访问".equals(operateStr)){
            			Intent intent= new Intent();        
    				    intent.setAction("android.intent.action.VIEW");    
    				    Uri content_url = Uri.parse(barcodeBean.getBarcode());   
    				    intent.setData(content_url);  
    				    startActivity(intent);
            		}
            	}else if(item==1){//选择删除
            		AlertDialog.Builder alertdb=new AlertDialog.Builder(HistoryActivity.this);
            		alertdb.setTitle("删除提示");
            		alertdb.setMessage("确定要删除该记录？");
                    alertdb.setPositiveButton("删除", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new DelSavedBarcodeTask().execute(barcodeBean.getId());
						}
					});
                    alertdb.setNegativeButton("取消", null);
                    alertdb.show();
            	}else{
            	}
           } 
           }); 
        alertdb.show();
		return false;
	}
	/**
	 * ListView中的选项被单击时的回调方法*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		// TODO Auto-generated method stub
		//获得选中项上的子类对象
		barcodeBean = (BarcodeBean)list.get(position);
		if(!Tools.isURL(barcodeBean.getBarcode())){
			Intent intent = new Intent(); 
			intent.setAction(Intent.ACTION_WEB_SEARCH); 
			intent.putExtra(SearchManager.QUERY,barcodeBean.getBarcode()); 
			startActivity(intent);
		}else{
			Intent intent= new Intent();        
		    intent.setAction("android.intent.action.VIEW");    
		    Uri content_url = Uri.parse(barcodeBean.getBarcode());   
		    intent.setData(content_url);  
		    startActivity(intent);
		}
	}
	/**
	 * 异步操作类——执行删除二维码扫描记录的操作
	 * @author sas
	 *
	 */
	class DelSavedBarcodeTask extends AsyncTask<Integer, Integer, String>{

		/**
		 * 后台执行查询保存的二维码扫描记录
		 */
		@Override
		protected String doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			BarcodeDBUtil barcodeDBUtil = new BarcodeDBUtil(HistoryActivity.this);
			boolean flag = barcodeDBUtil.deleteBarcodeRecord(params[0]);
			return flag+"";
		}
		/**
		 * 
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if("true".equals(result)){
				list.remove(location);//从list上移除该位置上的对象
				//实例化适配器
				adapter = new BarcodeAdapter(HistoryActivity.this, list);
				//为ListView添加适配器
				barcodeListView.setAdapter(adapter);
				Toast.makeText(HistoryActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(HistoryActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
			}
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = ProgressDialog.show(HistoryActivity.this,null,"正在删除…",true);
		}
	}
	/**
	 * 按下menu键时的回调方法
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// 为菜单添加子选项
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, getText(R.string.quit)).setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	/**
	 * 菜单中选项被选中时的回调方法
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case Menu.FIRST + 1:// 退出
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setIcon(android.R.drawable.ic_menu_help);
			dialog.setTitle("确定退出？");
			dialog.setPositiveButton("确定", new OnClickLiner_OK());
			dialog.setNegativeButton("取消", new OnClickLiner_Cancel());
			dialog.show();
			return true;
		}
		return false;
	}
	/**
	 * alertDialog监听事件
	 * 
	 * @author Administrator
	 * 
	 */
	class OnClickLiner_OK implements
			android.content.DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			HistoryActivity.this.finish();
		}

	}

	/**
	 * alertDialog监听事件
	 * 
	 * @author Administrator
	 * 
	 */
	class OnClickLiner_Cancel implements
			android.content.DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.cancel();
		}
	}
}
