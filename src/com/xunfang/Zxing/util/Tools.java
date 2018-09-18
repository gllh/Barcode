package com.xunfang.Zxing.util;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Tools {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	public static String formateDate(Date date){
		if(date!=null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format.format(date);
		}else return null;
	}
	public static boolean isURL(String url){
		if(url == null) return false;
		else {
			return url.matches("http://[^\\\\s]+");
		}
	}
}
