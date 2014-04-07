package com.shephertz.app42.common.push.plugin;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

public class App42GCMReceiver extends GCMBroadcastReceiver{
	@Override
	protected String getGCMIntentServiceClassName(Context context) { 
		return "com.shephertz.app42.common.push.plugin.App42GCMService"; 
	} 
}