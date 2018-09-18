package com.xunfang.Zxing.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * <p>Title：条码扫描</p>
 * <p>Description：数据库操作帮助类</p>
 * <p>Company：深圳市讯方通信技术有限公司 </p>
 * <p>Copyright: Copyright (c) 2012</p> 
 * @version 1.0.0.0
 * @author sas 
 */
public class BarcodeSqliteHelper extends SQLiteOpenHelper {

	//数据库名称
	private static String DATABASES_NAME = "barcode.db";
	//数据库版本
	private static int VERSION = 1;
	
	public BarcodeSqliteHelper(Context context){
		this(context, VERSION);
	}
	public BarcodeSqliteHelper(Context context,int version){
		this(context, DATABASES_NAME , null, version);
	}
	public BarcodeSqliteHelper(Context context, String name,CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 创建数据库时调用，包含表的创建和初始数据的插入
	 * */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("-----------------------------database onCreate----------------------");
		db.beginTransaction();//事务开始
		db.execSQL("create table barcode_recorde(id integer primary key autoincrement,barcode varchar(144) not null," +
				"savetime char(19) not null)");
		db.setTransactionSuccessful();//设置事务处理成功，不设置会自动回滚不提交
		db.endTransaction();//处理完成
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("-----------------------------database onUpgrade----------------------");
	}
}

