package com.xunfang.Zxing.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * <p>Title������ɨ��</p>
 * <p>Description�����ݿ����������</p>
 * <p>Company��������Ѷ��ͨ�ż������޹�˾ </p>
 * <p>Copyright: Copyright (c) 2012</p> 
 * @version 1.0.0.0
 * @author sas 
 */
public class BarcodeSqliteHelper extends SQLiteOpenHelper {

	//���ݿ�����
	private static String DATABASES_NAME = "barcode.db";
	//���ݿ�汾
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
	 * �������ݿ�ʱ���ã�������Ĵ����ͳ�ʼ���ݵĲ���
	 * */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("-----------------------------database onCreate----------------------");
		db.beginTransaction();//����ʼ
		db.execSQL("create table barcode_recorde(id integer primary key autoincrement,barcode varchar(144) not null," +
				"savetime char(19) not null)");
		db.setTransactionSuccessful();//����������ɹ��������û��Զ��ع����ύ
		db.endTransaction();//�������
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("-----------------------------database onUpgrade----------------------");
	}
}

