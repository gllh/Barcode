package com.xunfang.Zxing.Demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * <p>Title：3G物联网开发平台</p>
 * <p>Description：自定义的界面标题控件</p>
 * <p>Company：深圳市讯方通信技术有限公司 </p>
 * <p>Copyright: Copyright (c) 2012</p>
 * @version 1.0.0.0
 * @author 3G终端应用开发组 
 */
public class CommonTitle extends LinearLayout{
	
	private ImageView logo;//公司logo
	private TextView  title;//标题
	private ImageView revert;//返回按钮
  
    public CommonTitle(Context context) {  
        this(context, null);  
    }
  
    public CommonTitle(Context context,AttributeSet attrs) {  
        super(context,attrs);  
        // 导入布局  
        LayoutInflater.from(context).inflate(R.layout.common_title_item, this, true);
        //获得控件实例
        logo = (ImageView) findViewById(R.id.common_title_logo);  
        title = (TextView) findViewById(R.id.common_title_title);  
        revert = (ImageView) findViewById(R.id.common_title_revert);
    }  
    
    public ImageView getLogo() {
		return logo;
	}

	public void setLogo(ImageView logo) {
		this.logo = logo;
	}

	public TextView getTitle() {
		return title;
	}

	public void setTitle(TextView title) {
		this.title = title;
	}

	public ImageView getRevert() {
		return revert;
	}

	public void setRevert(ImageView revert) {
		this.revert = revert;
	}
}
