package com.shephertz.app42.common.push.plugin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.nokia.app42.push.MainActivity;
import com.example.android.nokia.app42.push.R;
import com.google.android.gcm.GCMRegistrar;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Log;
import com.shephertz.app42.paas.sdk.android.push.DeviceType;

/**
 * Provides the common implementation for the intent service regardless if it's
 * based on GCM or NNA.
 */
public class CommonIntentServiceImpl {
    private final String mTag;

    /**
     * Constructor.
     * 
     * @param tag The identification tag for logging.
     */
    public CommonIntentServiceImpl(final String tag) {
        mTag = tag;
        Log.i(mTag, "Service created.");
    }

    public String[] getSenderIds(Context context) {
        return new String[] {
            CommonUtilities.getSenderId(context)
        };
    }

    public void onRegistered(Context context, String registrationId,DeviceType deviceType) {
        Log.i(mTag, "Device registered with ID \"" + registrationId + "\"");
        registerWithApp42(registrationId,deviceType);
       
    }
    
    private void registerWithApp42(String regId,DeviceType deviceType) {
		App42Log.debug(" Registering on Server ....");
		App42API.buildPushNotificationService().storeDeviceToken(
				App42API.getLoggedInUser(), regId,deviceType, new App42CallBack() {
					@Override
					public void onSuccess(Object paramObject) {
						// TODO Auto-generated method stub
						App42Log.debug(" ..... Registeration Success ....");
					
					}

					@Override
					public void onException(Exception paramException) {
						App42Log.debug(" ..... Registeration Failed ....");
						App42Log.debug("storeDeviceToken :  Exception : on start up "
								+ paramException);

					}
				});

	}
    public void onUnregistered(Context context, String registrationId) {
        Log.i(mTag, "Device unregistered");
        CommonUtilities.displayMessage(context, context.getString(R.string.push_unregistered));
    }

    public void onMessage(Context context, Intent intent) {
        Log.i(mTag, "Received message. Extras: " + intent.getExtras());
        String message = intent.getExtras().getString("message");
        Log.i(mTag, "Received message "
				+ intent.getExtras().getString("message"));
		
        int messages = CommonUtilities.storeMessage(context, message);
        
        String text = messages > 1
                ? context.getString(R.string.push_message_many, messages)
                : context.getString(R.string.push_message);
        generateNotification(context, null, text, message, 0);
        CommonUtilities.processMessages(context);
    }

    public void onDeletedMessages(Context context, int total) {
        Log.i(mTag, "Received deleted messages notification");
        String message = context.getString(R.string.push_deleted, total);
        generateNotification(context, message, message, null, 1);
        CommonUtilities.displayMessage(context, message);
    }

    public void onError(Context context, String errorId) {
        Log.i(mTag, "Received error: " + errorId);
        CommonUtilities.displayMessage(context,
                context.getString(R.string.push_error, errorId));
    }

    public void onRecoverableError(Context context, String errorId) {
        Log.i(mTag, "Received recoverable error: " + errorId);
        CommonUtilities.displayMessage(context,
                context.getString(R.string.push_recoverable_error, errorId));
    }

    /**
     * Extracts the key-value pairs from the given Intent extras and returns
     * them in a string.
     * 
     * @param extras The Intent extras as Bundle.
     * @return The extracted data in a string.
     */
    private String intentExtrasToString(Bundle extras) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        
        for (String key : extras.keySet()) {
            sb.append(sb.length() <= 2 ? "" : ", ");
            sb.append(key).append("=").append(extras.get(key));
        }
        
        sb.append(" }");
        return sb.toString();
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private void generateNotification(Context context,
                                      String message,
                                      String text,
                                      String info,
                                      int id)
    {
        int icon = R.drawable.ic_stat_gcm;
        long when = System.currentTimeMillis();
        String activityName = null;
        Intent notificationIntent=null;
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Bundle aBundle=ai.metaData;
			activityName= aBundle.getString("onMessageOpen");
	         notificationIntent = new Intent(context, Class.forName(activityName));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 notificationIntent = new Intent(context,MainActivity.class);
		}
        if (message != null) {
            notificationIntent.putExtra("message", message);
        }
        
        // Set intent so it does not start a new activity
        notificationIntent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new NotificationCompat.Builder(context)
		.setContentTitle(context.getString(R.string.app_name))
		.setContentText(text).setContentInfo(info)
		.setTicker(text)
		.setContentIntent(intent)
		.setSmallIcon(icon)
		.setWhen(when)
		.setLights(Color.YELLOW, 1, 2).setAutoCancel(true).build();
        
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
    	notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
            .notify(id, notification);
    }
   	
}
