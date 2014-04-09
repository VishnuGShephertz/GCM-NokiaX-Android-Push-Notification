package com.example.android.nokia.app42.push;

import java.util.HashMap;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.shephertz.app42.common.push.plugin.CommonUtilities;
import com.shephertz.app42.common.push.plugin.NotificationsManager;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Log;
import com.shephertz.app42.paas.sdk.android.push.DeviceType;

public class MainActivity extends Activity {
	NotificationsManager mNotifications;
	private TextView txtMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtMsg=(TextView)findViewById(R.id.message);
		 App42API.initialize(
	                this,
	                "<Your App42 API Key>",
	                "Your App42 Secret Key");
		  App42API.setLoggedInUser("<Your User Id>");
		  mNotifications = NotificationsManager.getInstance(this);
		  if(checkIfSupported()){
			  registerWithApp42() ;
		  }
		  Intent startIntent = getIntent();
	        String message = null;
	        if (startIntent != null) {
	            message = startIntent.getStringExtra("message");
	        }
	        if (message != null) {
	            displayMessage(message);
	        }
	}

	
	private boolean checkIfSupported(){
	
		 if (mNotifications.getSupportedService() ==
	                NotificationsManager.SupportedNotificationServices.None)
	        {
	            ErrorDialog dialog = new ErrorDialog(this, R.string.notifications_not_supported);
	            dialog.show();
	            return false;
	        }
		 mNotifications.checkManifest();
		 return true;
		 
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	 /**
     * This class receives the messages from the notifications service and
     * provides the content for the UI.
     */
    private final BroadcastReceiver mMessageHandler = new BroadcastReceiver() {
        /**
         * @see android.content.BroadcastReceiver#onReceive(
         * android.content.Context, android.content.Intent)
         */
        @Override
        public void onReceive(Context context, Intent intent) {
           
            String action = intent.getAction();
            if (CommonUtilities.PROCESS_MESSAGES_ACTION.equals(action)) {
                processMessages();
            }
            else if (CommonUtilities.DISPLAY_MESSAGE_ACTION.equals(action)) {
                String message = intent.getStringExtra(CommonUtilities.EXTRA_MESSAGE);
                displayMessage(message);
            }
        }
    };
    
    private DeviceType getDeviceType(String serviceType){
		if(serviceType.equalsIgnoreCase("GCM"))
			return DeviceType.ANDROID;
		else
			return DeviceType.NOKIAX;
    }
	public  void registerWithApp42() {
		App42Log.debug(" ..... Registeration Check ....");
		HashMap<String, String> regMap=mNotifications.getRegistrationId();
			final String deviceRegId = regMap.get("regId");
			if (deviceRegId==null||deviceRegId.equals("")) {
				mNotifications.register();
				
			} else  {
					App42Log.debug(" Registering on Server ....");
					
						App42API.buildPushNotificationService().storeDeviceToken(App42API.getLoggedInUser(), deviceRegId,getDeviceType(regMap.get("type")), new App42CallBack() {
							
							@Override
							public void onSuccess(Object paramObject) {
								// TODO Auto-generated method stub
								App42Log.debug(" ..... Registeration Success ....");
								GCMRegistrar.setRegisteredOnServer(App42API.appContext, true);
							}
							
							@Override
							public void onException(Exception paramException) {
								App42Log.debug(" ..... Registeration Failed ....");
								App42Log.debug("storeDeviceToken :  Exception : on start up " +paramException);
								
							}
						});
					

				}
			}
	
	 /**
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CommonUtilities.DISPLAY_MESSAGE_ACTION);
        filter.addAction(CommonUtilities.PROCESS_MESSAGES_ACTION);
        registerReceiver(mMessageHandler, filter);
        displayMessage("Resumed.");
        processMessages();
    }
	  @Override
	    protected void onDestroy() {
	        mNotifications.onDestroy();
	        super.onDestroy();
	    }
	  
	  /**
	     * @see android.app.Activity#onPause()
	     */
	    @Override
	    protected void onPause() {
	        super.onPause();
	        unregisterReceiver(mMessageHandler);
	    }
	
	 /**
     * Displays the given message.
     * 
     * @param message The message to display.
     */
    private void displayMessage(String message) {
    	txtMsg.setText(message);
;
    }

    /**
     * Displays the last message, if one exists, and the number of new messages.
     */
    private void processMessages() {
        final int numOfNewMessages = CommonUtilities.clearMessages(this);
        final String message = CommonUtilities.popunReadMessages(this);
        StringBuilder sb = new StringBuilder();
        
        if (message != null) {
           sb.append("").append(numOfNewMessages).append(" New messages");
            sb.append("\n").append(message);
        }
        displayMessage(sb.toString());
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
    }

}
