package com.xunfang.Zxing.db;

import java.util.ArrayList;
import java.util.List;

import com.xunfang.Zxing.util.BarcodeBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * <p>
 * Title������ɨ��
 * </p>
 * <p>
 * Description�������¼�������ݿ������
 * </p>
 * <p>
 * Company��������Ѷ��ͨ�ż������޹�˾
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * 
 * 
 * @version 1.0.0.0
 * @author sas
 */
public class BarcodeDBUtil {
	private BarcodeSqliteHelper dbHelper;// �������ݿ����������

	public BarcodeDBUtil(Context context) {
		// ʵ�������ݿ����������
		this.dbHelper = new BarcodeSqliteHelper(context);
	}
	//signininfo(id integer primary key autoincrement,attendeeid integer not null," +
	//"state integer not null,signintime char(19) not null
	/**
	 * ���������¼
	 * 
	 * @param values
	 * @return ��ӳɹ�����true,���򷵻�false
	 */
	public boolean addBarcodeRecord(ContentValues values) {
		if (values == null)
			return false;
		SQLiteDatabase db = null;
		long id = -1;
		try {
			// ���SQLiteDatebaseʵ��
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			id = db.insert("barcode_recorde", null, values);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		} finally {
			db.close();
		}
		return id > 0;// id>0��ʾ����ɹ�
	}

	/**
	 * ��������¼�б�
	 * @return list ���ر���������¼
	 */
	public List<BarcodeBean> getBarcodeRecordList(){
		//ʵ�����б�
		ArrayList<BarcodeBean> list = new ArrayList<BarcodeBean>();
		//����SQLiteDatabase
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			//���SQLiteDatabaseʵ��
			db = dbHelper.getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
			//���SQLiteDatabaseʵ��
			db = dbHelper.getReadableDatabase();
		}	
		if(db != null){
			try {
				//����α�
				cursor = db.rawQuery("select id,barcode,savetime from barcode_recorde order by id asc", null);
				while(cursor.moveToNext()){
					//��ȡ����
					BarcodeBean barcodeBean = new BarcodeBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
					list.add(barcodeBean);
				}
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}finally{
				if(cursor != null) cursor.close();
				db.close();
			}
		}else {
			return null;
		}
		return list;
	}
	/**
	 * ����idɾ����ά���¼
	 * 
	 * @param id
	 *            ����Ķ�ά��ı��
	 */
	public boolean deleteBarcodeRecord(int id) {
		SQLiteDatabase db = null;
		try {
			// ���SQLiteDatebaseʵ��
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			db.execSQL("delete from barcode_recorde where id=?",
					new Object[] { id });
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		} finally {
			db.close();
		}
		return true;
	}
}
