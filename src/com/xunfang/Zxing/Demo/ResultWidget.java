package com.xunfang.Zxing.Demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * <p>
 * Title��3G����������ƽ̨
 * </p>
 * <p>
 * Description���Զ���ؼ�
 * </p>
 * <p>
 * Company��������Ѷ��ͨ�ż������޹�˾
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * 
 * @version 1.0.0.0
 * @author 3G�ն�Ӧ�ÿ�����
 */
public class ResultWidget extends LinearLayout {
	// ����ؼ�
	//private TextView textview0;
	//private TextView textview1;
	private TextView textview2;
	private TextView textview3;

	public ResultWidget(Context context) {
		this(context, null);
	}

	public ResultWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		// ���벼��
		LayoutInflater.from(context).inflate(
				R.layout.result_widget, this, true);
		// ��ÿؼ�ʵ��
		//textview0 = (TextView) findViewById(R.id.barcode_typestr);
		//textview1 = (TextView) findViewById(R.id.barcode_type);
		textview2 = (TextView) findViewById(R.id.barcode_contentstr);
		textview3 = (TextView) findViewById(R.id.barcode_content);
	}

	/*public TextView getTextview0() {
		return textview0;
	}

	public void setTextview0(TextView textview0) {
		this.textview0 = textview0;
	}

	public TextView getTextview1() {
		return textview1;
	}

	public void setTextview1(TextView textview1) {
		this.textview1 = textview1;
	}*/

	public TextView getTextview2() {
		return textview2;
	}

	public void setTextview2(TextView textview2) {
		this.textview2 = textview2;
	}

	public TextView getTextview3() {
		return textview3;
	}

	public void setTextview3(TextView textview3) {
		this.textview3 = textview3;
	}

}
