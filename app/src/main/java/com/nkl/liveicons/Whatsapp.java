package com.nkl.liveicons;

import android.content.res.XModuleResources;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;

public class Whatsapp implements IXposedHookZygoteInit, IXposedHookInitPackageResources {
	
	private static String MODULE_PATH = null;
	private static XSharedPreferences sharedPrefs;
	
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
        LiveIcons.debug("zygote initialized");
        
    }
    
    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
    	
    	if (!resparam.packageName.equals("com.whatsapp"))
            return;

        // Xposed Framework uses XSharedPreferences class,
        // which is same as AOSP SharedPreferences but without listeners support and is read-only
        sharedPrefs = new XSharedPreferences("com.nkl.liveicons", "MY_PREF");

        XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);

        // Gets the value at WHATSAPP_PULSE tag stored in MY_PREF preferences
        if(sharedPrefs.getInt("WHATSAPP_PULSE", 1 ) == 1) {

            // Sets "notifybar.png" as the drawable resource
            // in "com.whatsapp" package to be replaced
            resparam.res.setReplacement("com.whatsapp", "drawable", "notifybar",
                    // Forwards the "whatsapp_pulse.xml" resource from the module
                    modRes.fwd(R.drawable.whatsapp_pulse));

            LiveIcons.debug("whatsapp pulse notif ENABLED");
        }
        else {
        	LiveIcons.debug("whatsapp pulse notif DISABLED");
        }

    }

    

}
