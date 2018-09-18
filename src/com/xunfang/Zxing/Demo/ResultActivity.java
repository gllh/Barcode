package com.xunfang.Zxing.Demo;


import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xunfang.Zxing.db.BarcodeDBUtil;
import com.xunfang.Zxing.util.Tools;

public class ResultActivity extends Activity implements OnClickListener{
	//����ؼ�
	private ResultWidget resultWidget;
	private ImageView revert;//"����"��ť
	private TextView title;//����
	private Button operate;//"����"��"����"��ť
	private Button save;//"����"��ť
	private ProgressDialog dialog;//����
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//���ô��ڲ���
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.result);
        //�Զ��崰����ʽ
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.common_title);
		revert = (ImageView)findViewById(R.id.common_title_revert);
		title = (TextView)findViewById(R.id.common_title_title);
		operate  = (Button)findViewById(R.id.barcode_operate);
		save = (Button)findViewById(R.id.barcode_save);
		title.setText(R.string.result);
		revert.setOnClickListener(this);
		operate.setOnClickListener(this);
		save.setOnClickListener(this);
		resultWidget = (ResultWidget)findViewById(R.id.barcode_result);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent intent = getIntent();
		//String barcode_type = intent.getStringExtra("barcode_type");
		String barcode_content = intent.getStringExtra("barcode_content");
		resultWidget.getTextview2().setText((String)getText(R.string.barcode_content)+"��");
		resultWidget.getTextview3().setText(barcode_content);
		if(Tools.isURL(barcode_content)){
			operate.setText(getText(R.string.barcode_visit));
		}else{
			operate.setText(getText(R.string.barcode_search));
		}
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
			case R.id.barcode_save:
				new SaveBarcodeTask().execute("");
				break;
			case R.id.barcode_operate:
				Button button = (Button)view;
				String operateString = (String)button.getText();
				if(operateString.equals((String)getText(R.string.barcode_search))){
					Intent intent = new Intent(); 
					intent.setAction(Intent.ACTION_WEB_SEARCH); 
					intent.putExtra(SearchManager.QUERY,(String)resultWidget.getTextview3().getText()); 
					startActivity(intent);
				}else if(operateString.equals((String)getText(R.string.barcode_visit))){
					Intent intent= new Intent();        
				    intent.setAction("android.intent.action.VIEW");    
				    Uri content_url = Uri.parse((String)resultWidget.getTextview3().getText());   
				    intent.setData(content_url);  
				    startActivity(intent);
				}
				break;
			default:
				break;
			}
		}
	}
	/**
	 * �첽�����ࡪ��ִ�б����ά�����
	 * @author sas
	 *
	 */
	class SaveBarcodeTask extends AsyncTask<String, Integer, String>{

		/**
		 * ��ִ̨�б����ά��
		 */
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			BarcodeDBUtil barcodeDBUtil = new BarcodeDBUtil(ResultActivity.this);
			ContentValues values = new ContentValues();
			values.put("barcode", (String)resultWidget.getTextview3().getText());
			values.put("savetime", Tools.formateDate(new Date()));
			boolean flag = barcodeDBUtil.addBarcodeRecord(values);
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
				Toast.makeText(ResultActivity.this, "����ɹ�", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(ResultActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
			}
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = ProgressDialog.show(ResultActivity.this,null,"���ڱ��桭",true);
		}
	}
	/**
	 * ����menu��ʱ�Ļص�����
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// Ϊ�˵������ѡ��
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, getText(R.string.history)).setIcon(
				android.R.drawable.ic_menu_edit);
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, getText(R.string.quit)).setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	/**
	 * �˵���ѡ�ѡ��ʱ�Ļص�����
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case Menu.FIRST + 1:// �鿴��ʷ��¼
			Intent historyIntent = new Intent(ResultActivity.this,HistoryActivity.class);
			startActivity(historyIntent);
			return true;
		case Menu.FIRST + 2:// �˳�
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setIcon(android.R.drawable.ic_menu_help);
			dialog.setTitle("ȷ���˳���");
			dialog.setPositiveButton("ȷ��", new OnClickLiner_OK());
			dialog.setNegativeButton("ȡ��", new OnClickLiner_Cancel());
			dialog.show();
			return true;
		}
		return false;
	}
	/**
	 * alertDialog�����¼�
	 * 
	 * @author Administrator
	 * 
	 */
	class OnClickLiner_OK implements
			android.content.DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			ResultActivity.this.finish();
		}

	}

	/**
	 * alertDialog�����¼�
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
