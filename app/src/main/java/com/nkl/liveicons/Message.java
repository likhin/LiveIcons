package com.nkl.liveicons;

import android.content.res.XModuleResources;
import android.os.Build;
import android.util.Log;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;

public class Message implements IXposedHookZygoteInit, IXposedHookInitPackageResources {
	
	private static String MODULE_PATH = null;
	private static XSharedPreferences sharedPrefs;
	
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
        LiveIcons.debug("zygote initialized");
        
    }
    
    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
    	
    	if (!resparam.packageName.equals("com.android.mms"))
            return;

        // Xposed Framework uses XSharedPreferences class,
        // which is same as AOSP SharedPreferences but without listeners support and is read-only
        sharedPrefs = new XSharedPreferences("com.nkl.liveicons", "MY_PREF");
        
        XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);
        Log.i("LiveIcons" ,"MODULE_PATH = " + MODULE_PATH);
        LiveIcons.debug(MODULE_PATH);

        /*
        TODO : Fix issues with Samsung's Touchwiz

        update : Touchwiz's messaging app uses a custom notification icon. But replacing it
                does not seem to solve the issue. The app does not seem to use its own
                icon at all. The dingbats are probably using a framework resource?

        Summary of following code :
        if device manufacturer == Samsung
                replace "stat_notify_message"
        else
                replace "stat_notify_sms"
         */

        // The selected radio button's index is loaded from MY_PREF preferences file
        // flip notification index       = 0
        // breathing notification index  = 1

        if(Build.MANUFACTURER=="Samsung") {
            // Executes if manufacturer tag is Samsung in build.prop
            // Samsung Touchwiz ROMs have their own messaging app
            // Their notification icon has the name "stat_notify_message.png"

        	if(sharedPrefs.getInt("SAVED_RADIO_BUTTON_INDEX", 0 ) == 0) {

                // Sets "stat_notify_message.png" as the drawable resource
                // in "com.android.mms" package to be replaced
                resparam.res.setReplacement("com.android.mms", "drawable", "stat_notify_message",
                        // Forwards the "flip_sms.xml" resource from the module
                        modRes.fwd(R.drawable.flip_sms));

                LiveIcons.debug("sms flip notif ENABLED on SAMSUNG");
            }
            else {

                // Sets "stat_notify_message.png" as the drawable resource
                // in "com.android.mms" package to be replaced
                resparam.res.setReplacement("com.android.mms", "drawable", "stat_notify_message",
                        // Forwards the "breathing_sms.xml" resource from the module
                        modRes.fwd(R.drawable.breathing_sms));

                LiveIcons.debug("sms breathing notif ENABLED on SAMSUNG");
            }
        	
        }
        else {
            // Executes for all devices as default
            // Most devices use the stock messaging app
            // Their notification icon has the name "stat_notify_sms.png"

	        if(sharedPrefs.getInt("SAVED_RADIO_BUTTON_INDEX", 0 ) == 0) {

                // Sets "stat_notify_sms.png" as the drawable resource
                // in "com.android.mms" package to be replaced
	            resparam.res.setReplacement("com.android.mms", "drawable", "stat_notify_sms",
                        // Forwards the "flip_sms.xml" resource from the module
                        modRes.fwd(R.drawable.flip_sms));

	            LiveIcons.debug("sms flip notif ENABLED");
	        }
	        else {

                // Sets "stat_notify_sms.png" as the drawable resource
                // in "com.android.mms" package to be replaced
	            resparam.res.setReplacement("com.android.mms", "drawable", "stat_notify_sms",
                        // Forwards the "breathing_sms.xml" resource from the module
                        modRes.fwd(R.drawable.breathing_sms));

	            LiveIcons.debug("sms breathing notif ENABLED");
	        }
        }

    }

}
