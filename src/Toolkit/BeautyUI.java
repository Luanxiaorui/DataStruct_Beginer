package Toolkit;

import java.awt.Insets;

import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

public class BeautyUI {
	public  static void beautyUI(){
	    try
	    {
	        //如何解决切换输入法导致白屏的问题
	        System.setProperty("sun.java2d.noddraw", "true");
	        //设置属性即可：true表示使用ToolBar.background颜色实现纯
	        //色填充背景，BeautyEye中此属性默认是false
	        UIManager.put("ToolBar.isPaintPlainBackground", Boolean.TRUE);
	        //自定义JToolBar ui的border
	        Border bd = new org.jb2011.lnf.beautyeye.ch8_toolbar.BEToolBarUI.ToolBarBorder(
	                UIManager.getColor("ToolBar.shadow")     //Floatable时触点的颜色
	                , UIManager.getColor("ToolBar.highlight")//Floatable时触点的阴影颜色
	                , new Insets(0, 0, 0, 0));              //border的默认insets
	        UIManager.put("ToolBar.border",new BorderUIResource(bd));
	        //JTabbedPane左缩进
	        //改变InsetsUIResource参数的值即可实现
	        UIManager.put("TabbedPane.tabAreaInsets"
	                , new javax.swing.plaf.InsetsUIResource(0,0,0,0));
	        //设置本属性将改变窗口边框样式定义
	        BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
	        UIManager.put("RootPane.setupButtonVisible", false);
	        BeautyEyeLNFHelper.launchBeautyEyeLNF();
	    }
	    catch(Exception e)
	    {
	        //TODO exception
	    }
	}
}
