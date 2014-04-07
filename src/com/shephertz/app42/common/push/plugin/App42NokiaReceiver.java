package com.shephertz.app42.common.push.plugin;

import android.content.Context;

import com.nokia.push.PushBroadcastReceiver;

public class App42NokiaReceiver extends PushBroadcastReceiver{
	
	@Override
	protected String getPushIntentServiceClassName(Context context) {
		// TODO Auto-generated method stub
		return "com.shephertz.app42.common.push.plugin.App42NokiaService"; 
	} 
}