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
 * Title：条码扫描
 * </p>
 * <p>
 * Description：条码记录——数据库操作类
 * </p>
 * <p>
 * Company：深圳市讯方通信技术有限公司
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
	private BarcodeSqliteHelper dbHelper;// 定义数据库操作帮助类

	public BarcodeDBUtil(Context context) {
		// 实例化数据库操作帮助类
		this.dbHelper = new BarcodeSqliteHelper(context);
	}
	//signininfo(id integer primary key autoincrement,attendeeid integer not null," +
	//"state integer not null,signintime char(19) not null
	/**
	 * 保存条码记录
	 * 
	 * @param values
	 * @return 添加成功返回true,否则返回false
	 */
	public boolean addBarcodeRecord(ContentValues values) {
		if (values == null)
			return false;
		SQLiteDatabase db = null;
		long id = -1;
		try {
			// 获得SQLiteDatebase实例
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
		return id > 0;// id>0表示插入成功
	}

	/**
	 * 获得条码记录列表
	 * @return list 返回保存的条码记录
	 */
	public List<BarcodeBean> getBarcodeRecordList(){
		//实例化列表
		ArrayList<BarcodeBean> list = new ArrayList<BarcodeBean>();
		//定义SQLiteDatabase
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			//获得SQLiteDatabase实例
			db = dbHelper.getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
			//获得SQLiteDatabase实例
			db = dbHelper.getReadableDatabase();
		}	
		if(db != null){
			try {
				//获得游标
				cursor = db.rawQuery("select id,barcode,savetime from barcode_recorde order by id asc", null);
				while(cursor.moveToNext()){
					//提取数据
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
	 * 根据id删除二维码记录
	 * 
	 * @param id
	 *            保存的二维码的编号
	 */
	public boolean deleteBarcodeRecord(int id) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
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
