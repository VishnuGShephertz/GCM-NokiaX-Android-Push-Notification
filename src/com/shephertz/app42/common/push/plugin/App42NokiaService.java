package com.shephertz.app42.common.push.plugin;


/**
 * Copyright (c) 2014 Nokia Corporation and/or its subsidiary(-ies).
 * See the license text file delivered with this project for more information.
 */

import android.content.Context;
import android.content.Intent;

import com.nokia.push.PushBaseIntentService;
import com.shephertz.app42.paas.sdk.android.push.DeviceType;

/**
 * IntentService responsible for handling messages from Nokia Push
 * Notifications.
 */
public class App42NokiaService extends PushBaseIntentService {
    private static final String TAG = "NNASingleAPKSample/App42NokiaService";
    private CommonIntentServiceImpl mImplementation;

    /**
     * Constructor.
     */
    public App42NokiaService() {
        mImplementation = new CommonIntentServiceImpl(TAG);
    }

    /**
     * @see com.nokia.push.PushBaseIntentService#getSenderIds(android.content.Context)
     */
    @Override
    protected String[] getSenderIds(Context context) {
        return mImplementation.getSenderIds(context);
    }

    /**
     * @see com.nokia.push.PushBaseIntentService#onDeletedMessages(android.content.Context, int)
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        mImplementation.onDeletedMessages(context, total);
    }

    /**
     * @see com.nokia.push.PushBaseIntentService#onRecoverableError(android.content.Context, java.lang.String)
     */
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        mImplementation.onRecoverableError(context, errorId);
        return super.onRecoverableError(context, errorId);
    }

    /**
     * @see com.nokia.push.PushBaseIntentService#onRegistered(android.content.Context, java.lang.String)
     */
    @Override
    protected void onRegistered(Context context, String registrationId) {
        mImplementation.onRegistered(context, registrationId,DeviceType.NOKIAX);
    }

    /**
     * @see com.nokia.push.PushBaseIntentService#onUnregistered(android.content.Context, java.lang.String)
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        mImplementation.onUnregistered(context, registrationId);
    }

    /**
     * @see com.nokia.push.PushBaseIntentService#onMessage(android.content.Context, android.content.Intent)
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        mImplementation.onMessage(context, intent);
    }

    /**
     * @see com.nokia.push.PushBaseIntentService#onError(android.content.Context, java.lang.String)
     */
    @Override
    public void onError(Context context, String errorId) {
        mImplementation.onError(context, errorId);
    }
}
