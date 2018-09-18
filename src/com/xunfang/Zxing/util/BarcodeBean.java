package com.xunfang.Zxing.util;

import java.io.Serializable;

public class BarcodeBean implements Serializable{

	/**
	 * 二维码记录Bean
	 */
	private static final long serialVersionUID = 1L;
	private int id;//编号
	private String barcode;//条码内容
	private String savetime;//保存时间
	
	public BarcodeBean(int id,String barcode,String savetime){
		this.id = id;
		this.barcode = barcode;
		this.savetime = savetime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getSavetime() {
		return savetime;
	}
	public void setSavetime(String savetime) {
		this.savetime = savetime;
	}
}
